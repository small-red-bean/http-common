package com.redbean.httpcommon.comm;


public class NoRetryStrategy implements RetryStrategy {
    public boolean shouldRetry(Exception ex, ResponseMessage response) {
        return false;
    }
}
