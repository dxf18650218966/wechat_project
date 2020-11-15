package com.wechat.crypto;

import com.wechat.constant.SystemConst;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * Base64加密/解密
 * @Author dai
 * @Date 2020/2/14
 */
public class Base64Util {
    private static final Logger logger = LoggerFactory.getLogger(Base64Util.class);

    /**
     * 解密
     * @param decodeData 解密内容
     * @return
     */
    public static byte[] decode(String decodeData){
       return Base64.getDecoder().decode(decodeData);
    }

    /**
     * 解密
     * @param decodeData 解密内容
     * @return
     */
    public static String decodeStr(String decodeData){
        try {
            return new String(Base64.getDecoder().decode(decodeData), SystemConst.UTF8);
        }catch (UnsupportedEncodingException ex){
            logger.info("Base64解密转字符串异常");
            logger.error(ExceptionUtils.getStackTrace(ex));
            return "";
        }
    }

    /**
     * 加密
     * @param encodeData 加密内容
     * @return
     */
    public static String encode(byte[] encodeData){
        return Base64.getEncoder().encodeToString(encodeData);
    }

    /**
     * 加密
     * @param encodeData 加密内容
     * @return
     */
    public static String encode(String encodeData){
        return Base64.getEncoder().encodeToString(encodeData.getBytes());
    }
}
