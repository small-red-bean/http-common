package com.redbean.httpcommon.utils;

import com.redbean.httpcommon.comm.ResponseMessage;

import java.io.*;

public class IOUtils {

    public static String readStreamAsString(InputStream in, String charset)
            throws IOException {

        if (in == null) {
            return "";
        }

        Reader reader = null;
        Writer writer = new StringWriter();
        String result;

        char[] buffer = new char[1024];
        try {
            int n = -1;
            reader = new BufferedReader(new InputStreamReader(in, charset));
            while((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }

            result = writer.toString();
        } finally {
            in.close();
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        }

        return result;
    }

    public static byte[] readStreamAsByteArray(InputStream in)
            throws IOException {

        if (in == null) {
            return new byte[0];
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = in.read(buffer)) != -1) {
            output.write(buffer, 0, len);
        }
        output.flush();
        return output.toByteArray();
    }

    public static void closeResponseSilently(ResponseMessage response) {
        if (response != null) {
            try {
                response.close();
            } catch (IOException ioe) {
                LogUtil.getLog().error(ioe.getMessage(), ioe);
            }
        }
    }

    public static void closeSilently(Closeable ...closeables) {
        if(closeables != null) {
            for(Closeable closeable : closeables) {
                if(closeable != null) {
                    try {
                        closeable.close();
                    } catch (IOException ioe) {
                        LogUtil.getLog().error(ioe.getMessage(), ioe);
                    }
                }
            }
        }
    }
}

