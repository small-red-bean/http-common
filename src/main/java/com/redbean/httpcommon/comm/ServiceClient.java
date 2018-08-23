package com.redbean.httpcommon.comm;

import static com.redbean.httpcommon.utils.LogUtil.logException;

import com.redbean.httpcommon.exception.ClientException;
import com.redbean.httpcommon.utils.CodingUtils;
import com.redbean.httpcommon.utils.HttpUtil;
import com.redbean.httpcommon.utils.IOUtils;
import com.redbean.httpcommon.utils.LogUtil;
import org.apache.http.HttpMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public abstract class ServiceClient {
    protected ClientConfiguration config;

    protected ServiceClient(ClientConfiguration config) {
        this.config = config;
    }


    private String formatSlowRequestLog(RequestMessage request, ResponseMessage response,
                                        long useTimesMs) {
        return String.format("Request cost %d seconds, endpoint %s,"
                        + "method %s, statusCode %d, requestId %s.",
                useTimesMs / 1000, request.getAbsoluteUrl(),
                request.getMethod(), response.getStatusCode(),
                response.getRequestId());
    }

    private boolean shouldRetry(Exception exception, RequestMessage request,
                                ResponseMessage response, int retries,
                                RetryStrategy retryStrategy) {
        if (retries >= config.getMaxErrorRetry()) {
            return false;
        }

        if (retryStrategy.shouldRetry(exception, response)) {
            LogUtil.getLog().debug("Retrying on " + exception.getClass().getName() + ": " + exception.getMessage());
            return true;
        }
        return false;
    }

    private void handleResponse(ResponseMessage response, List<ResponseHandler> responseHandlers, ExecutionContext context)
            throws ClientException {
        for (ResponseHandler h : responseHandlers) {
            h.handle(response, context);
        }
    }

    private void handleRequest(RequestMessage message, List<RequestHandler> resquestHandlers, ExecutionContext context)
            throws ClientException {
        for (RequestHandler h : resquestHandlers) {
            h.handle(message, context);
        }
    }

    private Request buildRequest(RequestMessage requestMessage, ExecutionContext context)
            throws ClientException {

        Request request = new Request();
        /*set request method*/
        request.setMethod(requestMessage.getMethod());
        /*set request header*/
        request.setHeaders(requestMessage.getHeaders());

        String paramString = HttpUtil.paramToQueryString(requestMessage.getParameters(), context.getCharset());

        boolean requestIsPost = requestMessage.getMethod() == HttpMethod.POST;
        String uri = requestMessage.getAbsoluteUrl();
        boolean putParamsInUri = !requestIsPost;
        if (paramString != null && putParamsInUri) {
            uri += "?" + paramString;
        }
        /*set request url*/
        request.setUrl(uri);

        if (requestIsPost && requestMessage.getContent() == null && paramString != null) {
            // Put the param string to the request body if POSTing and
            // no content.
            try {
                byte[] buf = paramString.getBytes(context.getCharset());
                ByteArrayInputStream content = new ByteArrayInputStream(buf);
                request.setContent(content);
                request.setContentLength(buf.length);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("EncodingFailed", e);
            }
        } else {
            request.setContent(requestMessage.getContent());
            request.setContentLength(requestMessage.getContentLength());
        }

        return request;
    }

    private ResponseMessage sendRequestImpl(RequestMessage request, ExecutionContext context)
            throws ClientException {

        RetryStrategy retryStrategy = context.getRetryStrategy() != null ?
                context.getRetryStrategy() : this.getDefaultRetryStrategy();

        int retries = 0;
        ResponseMessage response = null;

        while (true) {
            try {
                /* The key four steps to send HTTP requests and receive HTTP responses. */

                // Step 1. Preprocess HTTP request.
                handleRequest(request, context.getResquestHandlers(), context);

                // Step 2. Build HTTP request with specified request parameters and context.
                Request httpRequest = buildRequest(request, context);

                // Step 3. Send HTTP request to api.
                long startTime = System.currentTimeMillis();
                response = sendRequestCore(httpRequest, context);
                long duration = System.currentTimeMillis() - startTime;
                if (duration > config.getSlowRequestsThreshold()) {
                    LogUtil.getLog().warn(formatSlowRequestLog(request, response, duration));
                }

                // Step 4. Preprocess HTTP response.
                handleResponse(response, context.getResponseHandlers(), context);

                return response;
            } catch (ClientException cex) {
                logException("[Client]Unable to execute HTTP request: ", cex);

                IOUtils.closeResponseSilently(response);

                if (!shouldRetry(cex, request, response, retries, retryStrategy)) {
                    throw cex;
                }
            } catch (Exception ex) {
                logException("[Unknown]Unable to execute HTTP request: ", ex);

                IOUtils.closeResponseSilently(response);

                throw new ClientException("ConnectionError:" + ex.getMessage(), ex);
            } finally {
                retries++;
            }
        }
    }

    public ResponseMessage sendRequest(RequestMessage request, ExecutionContext context)
            throws ClientException {

        CodingUtils.assertParameterNotNull(request, "request");
        CodingUtils.assertParameterNotNull(context, "context");

        try {
            return sendRequestImpl(request, context);
        } finally {
            // Close the request stream as well after the request is completed.
            try {
                request.close();
            } catch (IOException ex) {
                logException("Unexpected io exception when trying to close http request: ", ex);
                throw new ClientException("Unexpected io exception when trying to close http request: ", ex);
            }
        }
    }

    protected abstract ResponseMessage sendRequestCore(Request request, ExecutionContext context)
            throws IOException;


    protected abstract RetryStrategy getDefaultRetryStrategy();

    public abstract void shutdown();


    /**
     * Wrapper class based on {@link HttpMessage} that represents HTTP
     * request message to sw.
     */
    public static class Request extends HttpMesssage {
        private String uri;
        private HttpMethod method;

        public String getUri() {
            return this.uri;
        }

        public void setUrl(String uri) {
            this.uri = uri;
        }

        public HttpMethod getMethod() {
            return method;
        }

        public void setMethod(HttpMethod method) {
            this.method = method;
        }
    }
}

