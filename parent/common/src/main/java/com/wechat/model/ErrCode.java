package com.wechat.model;

/**
 * 返回响应错误码
 * @Author dai
 * @Date 2020/1/10
 */
public enum ErrCode {
    // ------------  通讯失败、业务失败，错误码 ---------
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
    THIRD_PARTY_INTERFACE_ERR("1010","third party interface exception !"),

    //  校验验证码错误
    VERIFY_COD_ERROR("1011","verify code error !"),

    // 手机号为空
    CELL_PHONE_NUMBER_IS_EMPTY("1012","cell phone number is empty !");




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
