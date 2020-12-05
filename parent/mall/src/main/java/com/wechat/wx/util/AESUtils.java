package com.wechat.wx.util;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;

/**
 * @Author dai
 * @Date 2020/11/29 
 */
public class AESUtils {
    public static boolean initialized = false;

    /**
     * AES解密
     */
    public static String decrypt(String cipherStr, String key, String iv) {
        byte[] content = Base64.decodeBase64(cipherStr);;
        byte[] keyByte = Base64.decodeBase64(key);;
        byte[] ivByte = Base64.decodeBase64(iv);
        initialize();
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            Key sKeySpec = new SecretKeySpec(keyByte, "AES");
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte));
            byte[] resultbyte = cipher.doFinal(content);
            return new String(resultbyte, "UTF-8");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    public static void initialize() {
        if (initialized){
            return;
        }
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }

    /**
     * 生成iv
     *
     * @param iv
     * @return
     * @throws Exception
     * @see
     */
    public static AlgorithmParameters generateIV(byte[] iv) throws Exception {
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(iv));
        return params;
    }

}
