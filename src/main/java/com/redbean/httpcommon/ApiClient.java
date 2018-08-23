package com.redbean.httpcommon;


import com.alibaba.fastjson.JSONObject;
import com.redbean.httpcommon.auth.Credentials;
import com.redbean.httpcommon.auth.DefaultCredentials;
import com.redbean.httpcommon.comm.*;
import com.redbean.httpcommon.hander.AesDecoderRespHandler;
import com.redbean.httpcommon.hander.AesEncoderReqHandler;
import com.redbean.httpcommon.utils.ApiConstants;
import com.redbean.httpcommon.utils.ApiHeaders;
import com.redbean.httpcommon.utils.IOUtils;
import com.redbean.httpcommon.utils.LogUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ApiClient {
    private Credentials credentials;
    private ServiceClient serviceClient;

    public ApiClient(String accessKeyId, String secretAccessKey) {
        this.credentials = new DefaultCredentials(accessKeyId, secretAccessKey, null);
        this.serviceClient = new DefaultServiceClient(new ClientConfiguration());
    }

    public String apiServlet(String endpoint, String cmd, Map<String, Object> parameters) {
        ResponseMessage responseMessage = null;
        String result = null;
        try {
            if (parameters == null) {
                parameters = new LinkedHashMap<String, Object>();
            }
            parameters.put("command", cmd);

            //step 1: build post body data
            JSONObject jo = new JSONObject();
            for (Iterator<String> iterator = parameters.keySet().iterator(); iterator.hasNext(); ) {
                String next = iterator.next();
                jo.put(next, parameters.get(next));
            }
            byte[] b = jo.toString().getBytes(ApiConstants.DEFAULT_CHARSET_NAME);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(b);

            //step 2: build request message
            RequestMessage requestMessage = new RequestMessage();
            requestMessage.setAbsoluteUrl(endpoint + "/apiServlet");
            requestMessage.setMethod(HttpMethod.POST);
            Map<String, String> headers = new LinkedHashMap<String, String>();
            headers.put(ApiHeaders.CONTENT_TYPE, "application/json");
            headers.put(ApiHeaders.API_CLIENT_ID, credentials.getAccessKeyId());
            requestMessage.setHeaders(headers);
            requestMessage.setContent(byteArrayInputStream);
            requestMessage.setContentLength(b.length);

            //step 3: build execution Context
            ExecutionContext executionContext = new ExecutionContext();
            executionContext.setCredentials(credentials);
            executionContext.setRetryStrategy(new DefaultRetryStrategy());
            executionContext.addRequestHandler(new AesEncoderReqHandler());
            executionContext.addResponseHandler(new AesDecoderRespHandler());

            //step 4: send req to api
            responseMessage = serviceClient.sendRequest(requestMessage, executionContext);

            //step 5: return result
            InputStream originalContent = responseMessage.getContent();
            byte[] r = IOUtils.readStreamAsByteArray(originalContent);
            result = new String(r, ApiConstants.DEFAULT_CHARSET_NAME);
        } catch (Exception e) {
            LogUtil.getLog().error(e.getMessage(), e);
        } finally {
            IOUtils.closeResponseSilently(responseMessage);
        }
        return result;
    }


    public static void main(String[] args) {
        ApiClient apiClient = new ApiClient("a", "m&8!L&(i$+%^@~*?");
        System.out.println("result: " + apiClient.apiServlet("http://localhost:8080", "demo", null));
    }
}
