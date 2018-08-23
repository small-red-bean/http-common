package com.redbean.httpcommon.utils;


public class CodingUtils {
    public static void assertParameterNotNull(Object param, String paramName) {
        if (param == null) {
            throw new NullPointerException("ParameterIsNull:" + paramName);
        }
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.length() == 0;
    }
}
