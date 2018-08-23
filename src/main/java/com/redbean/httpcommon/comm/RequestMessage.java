package com.redbean.httpcommon.comm;


import java.util.LinkedHashMap;
import java.util.Map;

public class RequestMessage extends HttpMesssage {
    /* The HTTP method to use when sending this request */
    private HttpMethod method = HttpMethod.GET;

    /* Use a LinkedHashMap to preserve the insertion order. */
    private Map<String, String> parameters = new LinkedHashMap<String, String>();

    /* The absolute url to which the request should be sent */
    private String absoluteUrl;

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getAbsoluteUrl() {
        return absoluteUrl;
    }

    public void setAbsoluteUrl(String absoluteUrl) {
        this.absoluteUrl = absoluteUrl;
    }
}
