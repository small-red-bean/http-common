package com.redbean.httpcommon.comm;


import com.redbean.httpcommon.exception.ClientException;

public interface RequestHandler {
    void handle(RequestMessage request, ExecutionContext context) throws ClientException;
}
