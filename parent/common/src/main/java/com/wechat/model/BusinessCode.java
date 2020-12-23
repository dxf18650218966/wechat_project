package com.wechat.model;

import com.alibaba.fastjson.JSONObject;

/**
 * 业务成功，业务码
 * @author dxf
 * @version 1.0
 * @date 2020/12/5 12:22
 */
public enum BusinessCode {
    // ------------  业务成功，业务码 ---------

    /**
     * 未注册
     */
    PLEASE_REGISTER("2000","please register !"),

    /**
     * 已注册
     */
    REGISTERED("2001","registered !");


    private String code;
    private String msg;

    BusinessCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static JSONObject json(BusinessCode businessCode){
        JSONObject jsonObject = new JSONObject(16);
        jsonObject.put("code",businessCode.getCode());
        jsonObject.put("msg",businessCode.getMsg());
        return jsonObject;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
