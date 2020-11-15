package com.wechat.crypto;

import com.wechat.constant.SystemConst;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * URL编码解码
 * @Author dai
 * @Date 2020/1/12
 */
public class UrlEncodeUtil {

    //---------------------编码----------------------
    public static String encodeByUtf(String url) {
        try {
            //采用utf-8字符集
            return URLEncoder.encode(url, SystemConst.UTF8).toLowerCase();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String encodeByGbk(String url) {
        try {
            //采用GBK字符集
            return URLEncoder.encode(url, SystemConst.GBK).toLowerCase();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }


    //---------------------解码----------------------
    public static String decodeByUtf(String url) {
        try {
            //采用utf-8字符集
            return URLDecoder.decode(url, SystemConst.UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String decodeByGbk(String url) {
        try {
            //采用GBK字符集
            return URLDecoder.decode(url, SystemConst.GBK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
