package com.redbean.httpcommon.comm;


import com.redbean.httpcommon.exception.ClientException;

public interface ResponseHandler {
    void handle(ResponseMessage response, ExecutionContext context) throws ClientException;
}
