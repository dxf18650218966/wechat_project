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
     * 例如：签名失败、参数格式校验错误、接口500
     * @param returnMsg 通讯失败原因
     * @return
     */
    public static Object returnFail(String returnMsg){
        Map<String, Object> map = new HashMap<String, Object>(16);
        map.put(SystemConst.RETURN_CODE , SystemConst.FAIL);
        map.put(SystemConst.RETURN_MSG , returnMsg);
        return map;
    }

    /**
     * 通讯成功但业务结果失败
     * @param errCode 枚举类型 ：错误码和错误描述
     * @return
     */
    public static Object resultFail(ErrCode errCode){
        Map<String, Object> map = new HashMap<String, Object>(16);
        map.put(SystemConst.RETURN_CODE , SystemConst.SUCCESS);
        map.put(SystemConst.RETURN_MSG , SystemConst.OK);
        map.put(SystemConst.RESULT_CODE , SystemConst.FAIL);
        map.put(SystemConst.ERR_CODE , errCode.getErrCode());
        map.put(SystemConst.ERR_MSG , errCode.getErrMsg());
        return map;
    }

    /**
     * 通讯成功但业务结果失败
     * @param errCode 错误码
     * @param errCodeDes 错误描述
     * @return
     */
    public static Object resultFail(String errCode, String errCodeDes){
        Map<String, Object> map = new HashMap<String, Object>(16);
        map.put(SystemConst.RETURN_CODE , SystemConst.SUCCESS);
        map.put(SystemConst.RETURN_MSG , SystemConst.OK);
        map.put(SystemConst.RESULT_CODE , SystemConst.FAIL);
        map.put(SystemConst.ERR_CODE , errCode);
        map.put(SystemConst.ERR_MSG,errCodeDes);
        return map;
    }

    /**
     * 通讯成功并且业务结果成功
     * @param obj 返回数据
     * @return
     */
    public static Object resultSuccess(Object obj){
        Map<String, Object> map = new HashMap<String, Object>(16);
        map.put(SystemConst.RETURN_CODE , SystemConst.SUCCESS);
        map.put(SystemConst.RETURN_MSG , SystemConst.OK);
        map.put(SystemConst.RESULT_CODE , SystemConst.SUCCESS);
        map.put(SystemConst.DATA , obj);
        return map;
    }
}
