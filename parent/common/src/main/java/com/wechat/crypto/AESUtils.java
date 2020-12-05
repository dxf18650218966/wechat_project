package com.wechat.crypto;

import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/** AES加密解密
 * 属于：对称加密算法
 * 特点：比des加解密更安全，速度更慢
 * 用法：通过一个公共密钥进行加解密
 *
 * 明文-->加密-->密文
 * 密文-->解密-->明文

 * @Author dai
 * @Date 2020/11/22
 */
public class AESUtils {

    private static final Logger LOG = LoggerFactory.getLogger(AESUtils.class);
    private static final String UTF8 = "UTF-8";
    private static final String AES = "AES";

    /**
     * 加密模式
     */
    public static final String ALGORITHM = "AES/CBC/PKCS7Padding";

    /**
     * 密钥
     */
    public static final String KEY = "MIVROAZ9I3K3J9X6";

    /**
     * 偏移量
     */
    public static final String IV = "RB9FIXS6NOI7NQAH";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 加密
     * @param plaintext 待加密内容
     * @return 密文
     */
    public static String encrypt( String plaintext) {
        byte[] result = null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM );
            SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(UTF8 ), AES );
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec);
            result = cipher.doFinal(plaintext.getBytes(UTF8 ));
        } catch (Exception e) {
            LOG.error("AES/CBC/PKCS7Padding加密异常:::");
            LOG.error(ExceptionUtils.getStackTrace(e));
        }
        // base64编码
        return Base64.encodeBase64String(result);
    }

    /**
     * 解密
     * @param ciphertext 待解密内容
     * @return 明文
     */
    public static String decrypt( String ciphertext) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM );
            SecretKeySpec keySpec = new SecretKeySpec( KEY.getBytes(UTF8 ), AES );
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.DECRYPT_MODE , keySpec, paramSpec);
            return new String(cipher.doFinal(Base64.decodeBase64(ciphertext)), UTF8 );
        } catch (Exception e) {
            LOG.error("AES/CBC/PKCS7Padding解密异常:::");
            LOG.error(ExceptionUtils.getStackTrace(e));
        }
        return "";
    }

    /**
     * 解密
     * @param ciphertext 待解密内容
     * @return 明文
     */
    public static String decrypt( String ciphertext, String key, String iv) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM );
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(UTF8 ), AES );
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv.getBytes(UTF8));
            cipher.init(Cipher.DECRYPT_MODE , keySpec, paramSpec);
            return new String(cipher.doFinal(Base64.decodeBase64(ciphertext)), UTF8 );
        } catch (Exception e) {
            LOG.error("AES/CBC/PKCS7Padding解密异常:::");
            LOG.error(ExceptionUtils.getStackTrace(e));
        }
        return "";
    }
}
