package com.wechat.wx.controller;

import cn.hutool.core.lang.Console;
import com.wechat.tool.XmlConvertUtil;
import com.wechat.wx.model.WxCommonConst;
import com.wechat.wx.service.AutoReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 自动回复（关键字、关注）
 * @Author dai
 * @Date 2020/11/7 
 */
@RestController
public class AutoReplyController {

    @Autowired
    private AutoReplyService autoReplyService;


    @PostMapping(value = "service",  produces = "text/plain;charset=UTF-8")
    public Object autoReply(@RequestBody String a){
        Map<String, String> requestMap = XmlConvertUtil.xmlToMap(a);
        // 开发者微信号
        String toUserName = requestMap.get("ToUserName");
        // 发送方帐号（一个OpenID）
        String fromUserName = requestMap.get("FromUserName");
        // 文本消息内容
        String content = requestMap.get("Content");
        // 消息类型    text文本、event事件
        String msgType = requestMap.get("MsgType");

        // ------ 文本消息 ------
        if( WxCommonConst.MESSAGE_TEXT .equals(msgType) ){
            return autoReplyService.keywordReply(toUserName, fromUserName, content);
        }

        // ------ 关注/取消关注事件、扫描带参数二维码事件 ------
        if( WxCommonConst.MESSAGE_EVENT .equals(msgType) ){
            String event = requestMap.get("Event");

            // :: 关注事件
            if(WxCommonConst.EVENT_SUBSCRIBE .equals(event)){
                Console.log("关注");
                return autoReplyService.foundReply(toUserName, fromUserName);

            }
            // :: 取消关注事件
            if(WxCommonConst.EVENT_UNSUBSCRIBE .equals(event)){
                Console.log("取消关注");

            }

            //扫描带参数二维码事件
            //MsgType 	消息类型，event
            //1. 用户未关注时，进行关注后的事件推送
            //EventKey 	事件KEY值，qrscene_为前缀，后面为二维码的参数值
            //Ticket 	二维码的ticket，可用来换取二维码图片
            //2. 用户已关注时的事件推送
            //MsgType 	消息类型，event
            //Event 	事件类型，SCAN
            //EventKey 	事件KEY值，是一个32位无符号整数，即创建二维码时的二维码scene_id
            //Ticket 	二维码的ticket，可用来换取二维码图片
        }
        return "success";
    }
}
