package com.wechat.model;

import com.wechat.constant.SystemConst;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回值封裝
 * @Author dai
 * @Date 2020/1/12
 */
public class ResponseUtil {

    /**
     * 通讯失败
     * 例如：签名失败、参数错误、未登录、接口500
     * @param errCode 枚举类型 ：错误码和错误描述
     * @return
     *     returnCode : 1001
     *     returnMsg : 未登录
     */
    public static ResponseModel returnFail(ErrCode errCode){
        return new ResponseModel(errCode.getErrCode(), errCode.getErrMsg());
    }
    public static ResponseModel returnFail(String errCode, String errMsg){
        return new ResponseModel(errCode, errMsg);
    }

    /**
     * 通讯成功但业务结果失败
     * @param errCode 枚举类型 ：错误码和错误描述
     * @return
     *     returnCode : SUCCESS
     *     returnMsg : OK
     *     resultCode : FAIL
     *     errcode : 1001
     *     errmsg : 余额不足
     */
    public static ResponseModel resultFail(ErrCode errCode){
        return new ResponseModel( SystemConst.SUCCESS , SystemConst.OK , SystemConst.FAIL ,
                errCode.getErrCode(), errCode.getErrMsg());
    }

    /**
     * 通讯成功但业务结果失败
     * @param errCode 错误码
     * @param errCodeDes 错误描述
     * @return
     */
    public static ResponseModel resultFail(String errCode, String errCodeDes){
        return new ResponseModel( SystemConst.SUCCESS , SystemConst.OK , SystemConst.FAIL , errCode, errCodeDes);
    }

    /**
     * 通讯成功并且业务结果成功
     * @param obj 返回数据
     * @return
     *     returnCode : SUCCESS
     *     returnMsg : OK
     *     resultCode : SUCCESS
     *     data ：响应数据
     */
    public static ResponseModel resultSuccess(Object obj){
        return new ResponseModel( SystemConst.SUCCESS , SystemConst.OK , SystemConst.SUCCESS , obj);
    }
}
