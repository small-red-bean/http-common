package com.redbean.httpcommon.comm;


import com.redbean.httpcommon.auth.Credentials;
import com.redbean.httpcommon.utils.ApiConstants;

import java.util.LinkedList;
import java.util.List;

public class ExecutionContext {
    private String charset = ApiConstants.DEFAULT_CHARSET_NAME;

    /* Retry strategy when HTTP request fails. */
    private RetryStrategy retryStrategy;

    /* The request handlers that handle request content in as a pipeline. */
    private List<RequestHandler> requestHandlers = new LinkedList<RequestHandler>();

    /* The response handlers that handle response message in as a pipeline. */
    private List<ResponseHandler> responseHandlers = new LinkedList<ResponseHandler>();

    private Credentials credentials;

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public RetryStrategy getRetryStrategy() {
        return retryStrategy;
    }

    public void setRetryStrategy(RetryStrategy retryStrategy) {
        this.retryStrategy = retryStrategy;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public List<ResponseHandler> getResponseHandlers() {
        return responseHandlers;
    }

    public void addResponseHandler(ResponseHandler handler) {
        responseHandlers.add(handler);
    }

    public void insertResponseHandler(int position, ResponseHandler handler) {
        responseHandlers.add(position, handler);
    }

    public void removeResponseHandler(ResponseHandler handler) {
        responseHandlers.remove(handler);
    }

    public List<RequestHandler> getResquestHandlers() {
        return requestHandlers;
    }

    public void addRequestHandler(RequestHandler handler) {
        requestHandlers.add(handler);
    }

    public void insertRequestHandler(int position, RequestHandler handler) {
        requestHandlers.add(position, handler);
    }

    public void removeRequestHandler(RequestHandler handler) {
        requestHandlers.remove(handler);
    }
}
