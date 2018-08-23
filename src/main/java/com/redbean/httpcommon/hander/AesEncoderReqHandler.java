package com.redbean.httpcommon.hander;


import com.redbean.httpcommon.comm.ExecutionContext;
import com.redbean.httpcommon.comm.RequestMessage;
import com.redbean.httpcommon.exception.ClientException;
import com.redbean.httpcommon.utils.AESUtil;
import com.redbean.httpcommon.utils.IOUtils;
import com.redbean.httpcommon.utils.LogUtil;
import com.redbean.httpcommon.comm.RequestHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AesEncoderReqHandler implements RequestHandler {
    public void handle(RequestMessage request, ExecutionContext context) throws ClientException {
        InputStream in = request.getContent();
        try {
            byte[] b = IOUtils.readStreamAsByteArray(in);
            b = AESUtil.encrypt2Bytes(b,context.getCredentials().getSecretAccessKey());
            request.setContent(new ByteArrayInputStream(b));
        } catch (IOException e) {
            LogUtil.getLog().error(e.getMessage(), e);
        } finally {
            IOUtils.closeSilently(in);
        }
    }
}
