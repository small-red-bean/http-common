package com.redbean.httpcommon.utils;

public interface ClientErrorCode {

    /**
     * 未知错误。
     */
     String UNKNOWN = "Unknown";

    /**
     * 未知域名。
     */
     String UNKNOWN_HOST = "UnknownHost";

    /**
     * 远程服务连接超时。
     */
     String CONNECTION_TIMEOUT = "ConnectionTimeout";

    /**
     * 远程服务socket读写超时。
     */
     String SOCKET_TIMEOUT = "SocketTimeout";

    /**
     * 远程服务socket异常。
     */
     String SOCKET_EXCEPTION = "SocketException";

    /**
     * 远程服务拒绝连接。
     */
     String CONNECTION_REFUSED = "ConnectionRefused";

    /**
     * 请求输入流不可重复读取。
     */
     String NONREPEATABLE_REQUEST = "NonRepeatableRequest";

}
