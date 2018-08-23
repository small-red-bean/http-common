package com.redbean.httpcommon.comm;


import com.redbean.httpcommon.exception.ClientException;
import com.redbean.httpcommon.utils.ApiHeaders;
import com.redbean.httpcommon.utils.ExceptionFactory;
import com.redbean.httpcommon.utils.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class DefaultServiceClient extends ServiceClient {
    protected static HttpRequestFactory httpRequestFactory = new HttpRequestFactory();
    protected CloseableHttpClient httpClient;
    protected HttpClientConnectionManager connectionManager;
    protected RequestConfig requestConfig;


    private static void readAndSetErrorResponse(InputStream originalContent, ResponseMessage response)
            throws IOException {
        byte[] contentBytes = IOUtils.readStreamAsByteArray(originalContent);
        response.setErrorResponseAsString(new String(contentBytes));
        response.setContent(new ByteArrayInputStream(contentBytes));
    }

    protected HttpClientConnectionManager createHttpClientConnectionManager() {
        SSLContext sslContext;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    return true;
                }

            }).build();

        } catch (Exception e) {
            throw new ClientException(e.getMessage());
        }

        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register(Protocol.HTTP.toString(), PlainConnectionSocketFactory.getSocketFactory())
                .register(Protocol.HTTPS.toString(), sslSocketFactory)
                .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connectionManager.setDefaultMaxPerRoute(config.getMaxConnections());
        connectionManager.setMaxTotal(config.getMaxConnections());
        connectionManager.setValidateAfterInactivity(config.getValidateAfterInactivity());
        connectionManager.setDefaultSocketConfig(SocketConfig.custom().
                setSoTimeout(config.getSocketTimeout()).setTcpNoDelay(true).build());
        if (config.isUseReaper()) {
            IdleConnectionReaper.setIdleConnectionTime(config.getIdleConnectionTime());
            IdleConnectionReaper.registerConnectionManager(connectionManager);
        }
        return connectionManager;
    }

    protected CloseableHttpClient createHttpClient(HttpClientConnectionManager connectionManager) {
        return HttpClients.custom().setConnectionManager(connectionManager)
                .disableContentCompression()
                .disableAutomaticRetries()
                .build();
    }

    public DefaultServiceClient(ClientConfiguration config) {
        super(config);
        this.connectionManager = createHttpClientConnectionManager();
        this.httpClient = createHttpClient(this.connectionManager);
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        requestConfigBuilder.setConnectTimeout(config.getConnectionTimeout());
        requestConfigBuilder.setSocketTimeout(config.getSocketTimeout());
        requestConfigBuilder.setConnectionRequestTimeout(config.getConnectionRequestTimeout());
        this.requestConfig = requestConfigBuilder.build();
    }

    @Override
    protected ResponseMessage sendRequestCore(Request request, ExecutionContext context) throws IOException {
        HttpRequestBase httpRequest = httpRequestFactory.createHttpRequest(request);
        HttpClientContext httpContext = HttpClientContext.create();
        httpContext.setRequestConfig(this.requestConfig);

        CloseableHttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpRequest, httpContext);
        } catch (IOException ex) {
            httpRequest.abort();
            throw ExceptionFactory.createNetworkException(ex);
        }
        return buildResponse(httpResponse);
    }

    protected static ResponseMessage buildResponse(CloseableHttpResponse httpResponse) throws IOException {
        assert(httpResponse != null);

        ResponseMessage response = new ResponseMessage();

        if (httpResponse.getStatusLine() != null) {
            response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        }

        if (httpResponse.getEntity() != null) {
            if (response.isSuccessful()) {
                response.setContent(httpResponse.getEntity().getContent());
            } else {
                readAndSetErrorResponse(httpResponse.getEntity().getContent(), response);
            }
        }

        for (Header header : httpResponse.getAllHeaders()) {
            if (ApiHeaders.CONTENT_LENGTH.equals(header.getName())) {
                response.setContentLength(Long.parseLong(header.getValue()));
            }
            response.addHeader(header.getName(), header.getValue());
        }

        return response;
    }


    @Override
    protected RetryStrategy getDefaultRetryStrategy() {
        return new DefaultRetryStrategy();
    }

    @Override
    public void shutdown() {
        IdleConnectionReaper.removeConnectionManager(this.connectionManager);
        this.connectionManager.shutdown();
    }
}
