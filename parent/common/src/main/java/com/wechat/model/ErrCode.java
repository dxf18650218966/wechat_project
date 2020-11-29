package com.wechat.model;

/**
 * 返回响应错误码
 * @Author dai
 * @Date 2020/1/10
 */
public enum ErrCode {
    // ------------ 1000 是代码问题提示            2000是业务提示 ----------------
    // 缺少请求参数
    MISSING_REQUEST_PARAMETERS("1000","missing request parameters !"),

    // 请求参数错误
    REQUEST_PARAMETER_ERROR("1001","request parameter error !"),

    // 请登陆
    PLEASE_LOG_IN("1003","please log in !"),

    // 系统内部错误
    SYSTEM_INTERNAL_ERROR("1004","system internal error !"),

    // 签名错误
    SIGN_ERROR("1005","sign error !"),

    // 图片上传失败
    IMG_UPLOAD_FAILED("1006","img upload failed !"),

    // 文件上传失败
    FILE_UPLOAD_FAILED("1007","file upload failed !"),

    // 第三方接口异常
    THIRD_PARTY_INTERFACE_ERR("1010","Third party interface exception !"),


    // ------------ 2000 业务提示 ------------
    // 手机号码为空
    CELL_PHONE_NUMBER_IS_EMPTY("2000","cell phone number is empty !"),

    //  校验验证码错误
    VERIFY_COD_ERROR("2001","verify code error !");


    private String errCode;
    private String errMsg;

    ErrCode(String errCode,String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    /**
     * 通过 key 获取 value
     * @param key
     * @return
     */
    public static String getValue(String key) {
        for (ErrCode ele : values()) {
            if(ele.getErrCode().equals(key)) {
                return ele.getErrMsg();
            }
        }
        return null;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }
}
