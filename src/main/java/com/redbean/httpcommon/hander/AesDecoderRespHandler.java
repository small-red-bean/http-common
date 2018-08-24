package com.redbean.httpcommon.hander;



import com.redbean.httpcommon.comm.ExecutionContext;
import com.redbean.httpcommon.comm.ResponseHandler;
import com.redbean.httpcommon.comm.ResponseMessage;
import com.redbean.httpcommon.exception.ClientException;
import com.redbean.httpcommon.utils.AESUtil;
import com.redbean.httpcommon.utils.IOUtils;
import com.redbean.httpcommon.utils.LogUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AesDecoderRespHandler implements ResponseHandler {
    public void handle(ResponseMessage response, ExecutionContext context) throws ClientException {
        InputStream in = response.getContent();
        try {
            byte[] b = IOUtils.readStreamAsByteArray(in);
            b = AESUtil.decrypt(b,context.getCredentials().getSecretAccessKey());
            response.setContent(new ByteArrayInputStream(b));
        } catch (IOException e) {
            LogUtil.getLog().error(e.getMessage(), e);
        } finally {
            IOUtils.closeSilently(in);
        }
    }
}
