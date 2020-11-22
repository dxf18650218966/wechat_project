package com.wechat.constant;

import java.time.format.DateTimeFormatter;

/**
 * 系统常量
 * @Author dai
 * @Date 2020/1/10
 */
public interface SystemConst {
    Integer SUCCES_CODE = 200;
    String SUCCESS = "SUCCESS";
    String FAIL = "FAIL";
    String OK = "OK";
    String XML = "xml";
    String HTTP = "http";

    String ZERO = "0";
    String ONE = "1";
    String TWO = "2";

    // 编码
    String UTF8 = "UTF-8";
    String GBK = "GBK";
    String MD5 = "MD5";


    // 日期格式
    String FORMAT1 = "yyyy-MM-dd HH:mm:ss";
    String FORMAT2 = "yyyy/MM/dd HH:mm:ss";
    String FORMAT3 = "yyyy-MM-dd HH:mm";
    String FORMAT4 = "yyyy/MM/dd HH:mm";
    String FORMAT5 = "yyyy-MM-dd";
    String FORMAT6 = "yyyy/MM/dd";
    String FORMAT7 = "HH:mm:ss";
    String FORMAT8 = "yyyyMMdd";
    String FORMAT9 = "HHmmss";
    String FORMAT10 = "yyyyMMddHHmmss";
    DateTimeFormatter FORMATTER1 = DateTimeFormatter.ofPattern(FORMAT1);
    DateTimeFormatter FORMATTER2 = DateTimeFormatter.ofPattern(FORMAT2);
    DateTimeFormatter FORMATTER3 = DateTimeFormatter.ofPattern(FORMAT3);
    DateTimeFormatter FORMATTER4 = DateTimeFormatter.ofPattern(FORMAT4);
    DateTimeFormatter FORMATTER5 = DateTimeFormatter.ofPattern(FORMAT5);
    DateTimeFormatter FORMATTER6 = DateTimeFormatter.ofPattern(FORMAT6);
    DateTimeFormatter FORMATTER7 = DateTimeFormatter.ofPattern(FORMAT7);
    DateTimeFormatter FORMATTER8 = DateTimeFormatter.ofPattern(FORMAT8);
    DateTimeFormatter FORMATTER9 = DateTimeFormatter.ofPattern(FORMAT9);
    DateTimeFormatter FORMATTER10 = DateTimeFormatter.ofPattern(FORMAT10);
}
