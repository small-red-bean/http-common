package com.redbean.httpcommon.comm;


public class ClientConfiguration {
    private static final int DEFAULT_MAX_RETRIES = 3;
    public static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = -1;
    public static final int DEFAULT_CONNECTION_TIMEOUT = 20 * 1000;
    public static final int DEFAULT_MAX_CONNECTIONS = 1024;
    public static final int DEFAULT_SOCKET_TIMEOUT = 20 * 1000;
    public static final int DEFAULT_VALIDATE_AFTER_INACTIVITY = 2 * 1000;
    public static final boolean DEFAULT_USE_REAPER = true;
    public static final long DEFAULT_IDLE_CONNECTION_TIME = 60 * 1000;
    public static final int DEFAULT_REQUEST_TIMEOUT = 5 * 60 * 1000;
    public static final long DEFAULT_SLOW_REQUESTS_THRESHOLD = 5 * 1000;


    private int maxErrorRetry = DEFAULT_MAX_RETRIES;
    private int connectionRequestTimeout = DEFAULT_CONNECTION_REQUEST_TIMEOUT;
    private int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
    private int socketTimeout = DEFAULT_SOCKET_TIMEOUT;
    private int maxConnections = DEFAULT_MAX_CONNECTIONS;
    private boolean useReaper = DEFAULT_USE_REAPER;
    private long idleConnectionTime = DEFAULT_IDLE_CONNECTION_TIME;
    private int requestTimeout = DEFAULT_REQUEST_TIMEOUT;
    private long slowRequestsThreshold = DEFAULT_SLOW_REQUESTS_THRESHOLD;

    /**
     * 返回一个值表示当可重试的请求失败后最大的重试次数。（默认值为3）
     * @return 当可重试的请求失败后最大的重试次数。
     */
    public int getMaxErrorRetry() {
        return maxErrorRetry;
    }

    /**
     * 设置一个值表示当可重试的请求失败后最大的重试次数。（默认值为3）
     * @param maxErrorRetry
     *          当可重试的请求失败后最大的重试次数。
     */
    public void setMaxErrorRetry(int maxErrorRetry) {
        this.maxErrorRetry = maxErrorRetry;
    }

    /**
     * 返回从连接池中获取连接的超时时间（单位：毫秒）。
     * 0表示无限等待。负值表示未定义，默认-1。
     * @return 从连接池中获取连接的超时时间。
     */
    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    /**
     * 设置从连接池中获取连接的超时时间（单位：毫秒）。
     * @param connectionRequestTimeout
     *          设置从连接池中获取连接的超时时间。
     */
    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    /**
     * 返回建立连接的超时时间（单位：毫秒）。
     * @return 建立连接的超时时间（单位：毫秒）。
     */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * 设置建立连接的超时时间（单位：毫秒）。
     * @param connectionTimeout
     *          建立连接的超时时间（单位：毫秒）。
     */
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * 返回通过打开的连接传输数据的超时时间（单位：毫秒）。
     * 0表示无限等待（但不推荐使用）。
     * @return 通过打开的连接传输数据的超时时间（单位：毫秒）。
     */
    public int getSocketTimeout() {
        return socketTimeout;
    }

    /**
     * 设置通过打开的连接传输数据的超时时间（单位：毫秒）。
     * 0表示无限等待（但不推荐使用）。
     * @param socketTimeout
     *          通过打开的连接传输数据的超时时间（单位：毫秒）。
     */
    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    /**
     * 返回允许打开的最大HTTP连接数。
     * @return 最大HTTP连接数。
     */
    public int getMaxConnections() {
        return maxConnections;
    }

    /**
     * 设置允许打开的最大HTTP连接数。
     * @param maxConnections
     *          最大HTTP连接数。
     */
    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    /**
     * 连接空闲该时间后，重用前检查该连接的有效性，默认2秒，单位毫秒。
     * @return 连接空闲时间
     */
    public int getValidateAfterInactivity() {
        return DEFAULT_VALIDATE_AFTER_INACTIVITY;
    }

    /**
     * 查看是否使用{@link IdleConnectionReaper}管理过期连接。
     */
    public boolean isUseReaper() {
        return useReaper;
    }

    /**
     * 设置是否使用{@link IdleConnectionReaper}管理过期连接。
     */
    public void setUseReaper(boolean useReaper) {
        this.useReaper = useReaper;
    }

    /**
     * 获取关闭空闲连接的时长。
     * @return 关闭空闲连接的时长。
     */
    public long getIdleConnectionTime() {
        return idleConnectionTime;
    }

    /**
     * 设置空闲连接的时长，连接空闲该时间后关闭，单位毫秒，默认60秒。
     */
    public void setIdleConnectionTime(long idleConnectionTime) {
        this.idleConnectionTime = idleConnectionTime;
    }

    /**
     * 设置请求超时时间，单位毫秒，默认5分钟。
     */
    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    /**
     * 获取请求超时时间，单位毫秒。
     */
    public int getRequestTimeout() {
        return requestTimeout;
    }

    /**
     * 设置慢请求阈值，用时超过该阈值的请求将打印到日志中，单位毫秒，默认5分钟。
     */
    public long getSlowRequestsThreshold() {
        return slowRequestsThreshold;
    }

    /**
     * 获取慢请求阈值，用时超过该阈值的请求将打印到日志中，单位毫秒。
     */
    public void setSlowRequestsThreshold(long slowRequestsThreshold) {
        this.slowRequestsThreshold = slowRequestsThreshold;
    }
}
