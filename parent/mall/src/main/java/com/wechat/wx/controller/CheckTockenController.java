package com.wechat.wx.controller;

import com.wechat.crypto.SHA1Util;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * 微信验证开发者服务器，验证消息的确来自微信服务器
 * @Author dai
 * @Date 2020/11/1 
 */
@RestController
public class CheckTockenController {
    private static final String TOKEN = "wechat_token";

    /**
     * 验证token
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @param echostr 随机字符串
     * 开发者通过检验signature对请求进行校验，若确认此次GET请求来自微信服务器，请原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败。
     * 加密/校验流程如下：
     *         1）将token、timestamp、nonce三个参数进行字典序排序
     *         2）将三个参数字符串拼接成一个字符串进行sha1加密
     *         3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
     * @return
     */
    @GetMapping("service")
    public String checkTocken(@RequestParam("signature") String signature,@RequestParam("timestamp") String timestamp,
                               @RequestParam("nonce") String nonce,@RequestParam("echostr") String echostr) {

        //排序
        String[] arr = {TOKEN, timestamp, nonce};
        Arrays.sort(arr);

        // 拼接
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }

        // 加密
        String s = SHA1Util.encrypt(content.toString());

        // 对比
        if (s.equals(signature)) {
            return echostr;
        }
        return "";
    }
}