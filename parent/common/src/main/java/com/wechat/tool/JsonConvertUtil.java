package com.wechat.tool;


/**
 * json转换
 * @author dxf
 */
public class JsonConvertUtil {
    /**-----------------------------FastJson----------------------------
    对象 转--> JSON字符串
    String jsonStr = JSON.toJSONString(对象)

    JSON字符串 转--> java对象
    对象类型 obj = JSON.parseObject(jsonStr,对象运行时类)

    JSON字符串 转--> JSONObject
    JSONObject jsonObject = JSON.parseObject(jsonStr);
    String value = jsonObject.get("属性");

    JSON字符串 转--> 对象集合
    List<对象> list = JSON.parseArray(jsonStr,对象运行时类)         */


    /**
     * json格式：{"userId":"dxf"}
     * 有时候会遇到 {\"userId\":\"dxf\"}、 "{\"userId\":\"dxf\"}"
     * 去除反斜杠 StringEscapeUtils.unescapeJava(jsonStr)
     * 去除前后双引 jsonStr.substring(1, jsonStr.length()-1);
     */




}
