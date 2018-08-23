package com.redbean.httpcommon.utils;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public final class LogUtil {
    private static final Log log = LogFactory.getLog(ApiConstants.LOGGER_PACKAGE_NAME);

    public static Log getLog() {
        return log;
    }

    public static <ExType> void logException(String messagePrefix, ExType ex) {

        assert(ex instanceof Exception);

        String detailMessage = messagePrefix + ((Exception) ex).getMessage();
        log.warn(detailMessage);
    }
}
