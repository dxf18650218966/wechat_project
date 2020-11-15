package com.wechat.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author dai
 * @Date 2020/11/1 
 */
public class SHA1Util {

    /**
     * sha1加密操作
     */
    public static String encrypt(String content) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(content.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++){
                hexString.append(String.format("%02X", 0xFF & messageDigest[i]));
            }
            return hexString.toString().toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
