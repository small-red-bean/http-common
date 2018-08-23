package com.redbean.httpcommon.comm;


public enum Protocol {

    HTTP("http"),

    HTTPS("https");

    private String protocol;

    Protocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        return protocol;
    }
}
