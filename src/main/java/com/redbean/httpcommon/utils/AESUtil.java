package com.redbean.httpcommon.utils;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("长度不是偶数");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    public static String Decrypt(String sSrc, String sKey) throws Exception {
        try {
            byte[] raw = sKey.getBytes(ApiConstants.DEFAULT_CHARSET_NAME);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = hex2byte(sSrc.getBytes());
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original);
        } catch (Exception ex) {
            LogUtil.getLog().error(ex.getMessage(), ex);
        }
        return null;
    }

    public static String Encrypt(String sSrc, String sKey) {
        try {
            byte[] raw = sKey.getBytes(ApiConstants.DEFAULT_CHARSET_NAME);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes());
            return byte2hex(encrypted).toLowerCase();
        }catch (Exception ex) {
            LogUtil.getLog().error(ex.getMessage(), ex);
        }
        return null;
    }

    public static byte[] encrypt2Bytes(byte[] src, String sKey) {
        try {
            byte[] raw = sKey.getBytes(ApiConstants.DEFAULT_CHARSET_NAME);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(src);
            return encrypted;
        } catch (Exception ex) {
            LogUtil.getLog().error(ex.getMessage(), ex);
        }

        return null;
    }

    public static byte[] decrypt2Bytes(byte[] src, String sKey) {
        try {
            byte[] raw = sKey.getBytes(ApiConstants.DEFAULT_CHARSET_NAME);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = src;
            byte[] original = cipher.doFinal(encrypted1);
            return original;
        } catch (Exception ex) {
            LogUtil.getLog().error(ex.getMessage(), ex);
        }
        return null;
    }
}
