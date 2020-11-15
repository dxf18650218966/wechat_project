package com.wechat.model;

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

}
