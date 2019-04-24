package com.example.demo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author : chpyue@foxmail.com
 * @date :2018/11/8 10:47
 * @Description: MD5 工具类
 */
public class MD5Util {

    private static final String SALT = "cdio";

    public static String encode(String password) {
        password = password + SALT;
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        char[] charArray = password.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }

            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static void main(String[] args) {
        System.out.println(MD5Util.encode("admin"));
    }

}