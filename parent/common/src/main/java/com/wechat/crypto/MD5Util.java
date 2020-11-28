package com.wechat.crypto;

import java.security.MessageDigest;

/**
 * MD5加密
 * @Author dai
 * @Date 2020/1/12
 */
public class MD5Util {
    /**
     * 十六进制下数字到字符的映射数组
     */
    private final static String[] HEX_DIGITS = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};

    /**
     * MD5加密--大写（32位）
     * @param str 加密字符串
     * @return
     */
    public static String md5UpperCase(String str){
        return encodeByMD5(str);
    }
    /**
     * MD5加密--小写（32位）
     * @param str 加密字符串
     * @return
     */
    public static String md5LowerCase(String str){
        return encodeByMD5(str).toLowerCase();
    }

    // ---------------------------------------源码----------------------------------------

    /**
     * 对字符串进行MD5编码
     * @param str
     * @return
     */
    private static String encodeByMD5(String str){
        if (str!=null) {
            try {
                //创建具有指定算法名称的信息摘要
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                //使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                //这里一定要指定字符集，否则会导致idea等工具对中文md5正常，但是线上就和本地结果不一致，这是编码问题
                byte[] results = md5.digest(str.getBytes("UTF-8"));
                //将得到的字节数组变成字符串返回
                String result = byteArrayToHexString(results);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    /**
     * 字节数组转化成十六进制字符串
     * @param b
     * @return
     */
    private static String byteArrayToHexString(byte[] b){
        StringBuffer resultSb = new StringBuffer();
        for(int i=0;i<b.length;i++){
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * 将一个字节转化成十六进制形式的字符串
     * @param b
     * @return
     */
    private static String byteToHexString(byte b){
        int n = b;
        if(n<0){
            n=256+n;
        }
        int d1 = n/16;
        int d2 = n%16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }
}
