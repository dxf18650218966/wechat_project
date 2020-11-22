package com.wechat.model;

import lombok.Data; 
import java.io.Serializable;

/**
 * 返回值定义
 * @Author dai
 * @Date 2020/11/22 
 */
@Data
public class ResponseModel implements Serializable {
    private static final long serialVersionUID = -8554993608663807799L;

    /**
     * 通讯码
     * 例如：签名失败、参数错误、未登录、接口500
     */
    String returnCode;

    /**
     * 通讯信息
     */
    String returnMsg;

    /**
     * 业务结果
     */
    String resultCode;

    /**
     * 错误码
     */
    String errcode;

    /**
     * 错误信息
     */
    String errmsg;

    /**
     * 业务结果成功后返回的数据
     */
    Object data;

    /**
     * 通讯失败
     */
    public ResponseModel(String returnCode, String returnMsg) {
        this.returnCode = returnCode;
        this.returnMsg = returnMsg;
    }

    /**
     * 通讯成功但业务结果失败
     */
    public ResponseModel(String returnCode, String returnMsg, String resultCode, String errcode, String errmsg) {
        this.returnCode = returnCode;
        this.returnMsg = returnMsg;
        this.resultCode = resultCode;
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    /**
     *  通讯成功并且业务结果成功
     */
    public ResponseModel(String returnCode, String returnMsg, String resultCode, Object data) {
        this.returnCode = returnCode;
        this.returnMsg = returnMsg;
        this.resultCode = resultCode;
        this.data = data;
    }
}
