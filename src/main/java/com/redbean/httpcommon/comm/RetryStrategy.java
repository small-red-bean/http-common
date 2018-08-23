package com.redbean.httpcommon.comm;


public interface RetryStrategy {
    boolean shouldRetry(Exception ex, ResponseMessage response);
}
