package com.redbean.httpcommon.utils;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class AESUtil {

    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";


    private static Key toKey(String key) throws Exception {
        return new SecretKeySpec(key.getBytes(ApiConstants.DEFAULT_CHARSET_NAME), KEY_ALGORITHM);
    }

    public static byte[] encrypt(byte[] data, String key) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, toKey(key));
            byte[] encrypted = cipher.doFinal(data);
            System.out.println(byte2hex(encrypted).toLowerCase());
            return byte2hex(encrypted).toLowerCase().getBytes(ApiConstants.DEFAULT_CHARSET_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(byte[] data, String key) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, toKey(key));
            return cipher.doFinal(hex2byte(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String initSecretKey() {
        KeyGenerator kg;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            kg.init(128);
            SecretKey secretKey = kg.generateKey();
            return byte2hex(secretKey.getEncoded()).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    private static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("长度不是偶数");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    public static void main(String[] args) throws Exception{
        byte[] b = AESUtil.encrypt("hello world".getBytes(ApiConstants.DEFAULT_CHARSET_NAME), "m&8!L&(i$+%^@~*?");
        System.out.println(new String(AESUtil.decrypt(b, "m&8!L&(i$+%^@~*?"),ApiConstants.DEFAULT_CHARSET_NAME));
    }
}
