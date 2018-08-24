package com.redbean.httpcommon.comm;


import com.redbean.httpcommon.exception.ClientException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

import java.util.Iterator;

public class HttpRequestFactory {

    public HttpRequestBase createHttpRequest(ServiceClient.Request request) {
        String uri = request.getUri();

        HttpRequestBase httpRequest;
        HttpMethod method = request.getMethod();

        if (method == HttpMethod.POST) {
            HttpPost postMethod = new HttpPost(uri);
            if (request.getContent() != null) {
                postMethod.setEntity(new RepeatableInputStreamEntity(request));
            }
            httpRequest = postMethod;
        } else if (method == HttpMethod.GET) {
            httpRequest = new HttpGet(uri);
        } else {
            throw new ClientException("Unknown HTTP method name: " + method.toString());
        }

        for (Iterator<String> iterator = request.getHeaders().keySet().iterator(); iterator.hasNext(); ) {
            String next = iterator.next();
            httpRequest.setHeader(next, request.getHeaders().get(next));
        }
        return httpRequest;
    }
}
