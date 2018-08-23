package com.redbean.httpcommon.comm;


import com.redbean.httpcommon.exception.ClientException;
import com.redbean.httpcommon.utils.ClientErrorCode;
import org.apache.http.HttpStatus;

public class DefaultRetryStrategy implements RetryStrategy {
    public boolean shouldRetry(Exception ex, ResponseMessage response) {
        if (ex instanceof ClientException) {
            String errorCode = ((ClientException) ex).getErrorCode();
            if (errorCode.equals(ClientErrorCode.CONNECTION_TIMEOUT) ||
                    errorCode.equals(ClientErrorCode.SOCKET_TIMEOUT) ||
                    errorCode.equals(ClientErrorCode.CONNECTION_REFUSED) ||
                    errorCode.equals(ClientErrorCode.UNKNOWN_HOST) ||
                    errorCode.equals(ClientErrorCode.SOCKET_EXCEPTION)) {
                return true;
            }

            // Don't retry when request input stream is non-repeatable
            if (errorCode.equals(ClientErrorCode.NONREPEATABLE_REQUEST)) {
                return false;
            }
        }

        if (response != null) {
            int statusCode = response.getStatusCode();
            if (statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR ||
                    statusCode == HttpStatus.SC_SERVICE_UNAVAILABLE) {
                return true;
            }
        }

        return false;
    }
}
