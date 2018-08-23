package com.redbean.httpcommon.comm;

import com.redbean.httpcommon.utils.ApiHeaders;

public class ResponseMessage extends HttpMesssage {
    private static final int HTTP_SUCCESS_STATUS_CODE = 200;
    private String errorResponseAsString;
    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getRequestId() {
        return getHeaders().get(ApiHeaders.API_HEADER_REQUEST_ID);
    }

    public String getErrorResponseAsString() {
        return errorResponseAsString;
    }

    public void setErrorResponseAsString(String errorResponseAsString) {
        this.errorResponseAsString = errorResponseAsString;
    }

    public boolean isSuccessful(){
        return statusCode / 100 == HTTP_SUCCESS_STATUS_CODE / 100;
    }

}
