package com.redbean.httpcommon.exception;


import com.redbean.httpcommon.utils.ClientErrorCode;

public class ClientException extends RuntimeException {
    private String errorMessage;
    private String requestId;
    private String errorCode;

    public ClientException() {
        super();
    }

    public ClientException(String errorMessage) {
        this(errorMessage, null);
    }

    public ClientException(Throwable cause) {
        this(null, cause);
    }

    public ClientException(String errorMessage, Throwable cause) {
        super(null, cause);
        this.errorMessage = errorMessage;
        this.errorCode = ClientErrorCode.UNKNOWN;
        this.requestId = "Unknown";
    }

    public ClientException(String errorMessage, String errorCode, String requestId) {
        this(errorMessage, errorCode, requestId, null);
    }

    public ClientException(String errorMessage, String errorCode, String requestId, Throwable cause) {
        this(errorMessage, cause);
        this.errorCode = errorCode;
        this.requestId = requestId;
    }

    @Override
    public String getMessage() {
        return errorMessage
                + "\n[ErrorCode]: " + errorCode != null ? errorCode : ""
                + "\n[RequestId]: " + requestId != null ? requestId : "";
    }

    public String getErrorCode() {
        return errorCode;
    }
}
