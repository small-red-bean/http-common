package com.redbean.httpcommon.auth;


public interface Credentials {
    String getAccessKeyId();
    String getSecretAccessKey();
    String getSecurityToken();
}
