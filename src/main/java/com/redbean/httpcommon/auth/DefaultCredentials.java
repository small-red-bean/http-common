package com.redbean.httpcommon.auth;


import com.redbean.httpcommon.exception.InvalidCredentialsException;

public class DefaultCredentials implements Credentials {
    private final String accessKeyId;
    private final String secretAccessKey;
    private final String securityToken;

    public DefaultCredentials(String accessKeyId, String secretAccessKey, String securityToken) {
        if(accessKeyId == null || "".equals(accessKeyId.trim())) {
            throw new InvalidCredentialsException("Access key id should not be null or empty.");
        }

        if(secretAccessKey == null || "".equals(secretAccessKey.trim())) {
            throw new InvalidCredentialsException("Secret access key should not be null or empty.");
        }

        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
        this.securityToken = securityToken;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    public String getSecurityToken() {
        return securityToken;
    }
}
