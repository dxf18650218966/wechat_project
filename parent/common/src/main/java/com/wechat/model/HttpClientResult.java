package com.wechat.model;

import cn.hutool.core.util.StrUtil;
import com.wechat.constant.SystemConst;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * http请求结果
 * @Author dai
 * @Date 2020/1/12
 */
@Data
@AllArgsConstructor
public class HttpClientResult implements Serializable {
    private static final long serialVersionUID = -3049881913900959620L;
    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应数据
     */
    private String data;

    /**
     * 判断请求结果是否成功，有没获取到响应数据
     * @param httpClientResult
     * @return 成功返回true
     */
    public static Boolean businessResult (HttpClientResult httpClientResult){
        return SystemConst.SUCCES_CODE.equals(httpClientResult.getCode()) && StrUtil.isNotBlank(httpClientResult.getData());
    }
}
