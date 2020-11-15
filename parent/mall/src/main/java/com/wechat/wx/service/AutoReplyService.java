package com.wechat.wx.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechat.constant.SystemConst;
import com.wechat.tool.DateUtil;
import com.wechat.tool.XmlConvertUtil;
import com.wechat.wx.entity.AutoReplyBean;
import com.wechat.wx.mapper.AutoReplyMapper;
import com.wechat.wx.model.RedisKeyConst;
import com.wechat.wx.model.WxCommonConst;
import com.wechat.wx.util.MaterialImgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 自动回复
 * @Author dai
 * @Date 2020/11/7 
 */
@Service
public class AutoReplyService {
    @Autowired
    private AutoReplyMapper autoReplyMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private MaterialImgUtil mediaUploadUtil;


    /**
     * 关键字回复
     * @param toUserName 开发者微信号
     * @param fromUserName 发送方帐号（一个OpenID）
     * @param keyword 关键字
     * @return
     */
    public Object keywordReply(String toUserName, String fromUserName, String keyword){
        // 查询自动回复配置
        AutoReplyBean autoReplyBean = autoReplyMapper.selectOne(new QueryWrapper<AutoReplyBean>()
                // 所属公众号
                .eq("gzh", toUserName)
                // 是否生效  （0否 1是）
                .eq("has_valid", SystemConst.ONE)
                // 回复类型  （0关注回复 1关键字回复）
                .eq("reply_type", SystemConst.ONE)
                // 关键字
                .like("keyword", keyword));

        return replyTemplate(autoReplyBean, toUserName, fromUserName);
    }

    /**
     * 关注回复
     * @param toUserName 开发者微信号
     * @param fromUserName 发送方帐号（一个OpenID）
     * @return
     */
    public Object foundReply(String toUserName, String fromUserName){
        // 查询自动回复配置
        AutoReplyBean autoReplyBean = autoReplyMapper.selectOne(new QueryWrapper<AutoReplyBean>()
                // 所属公众号
                .eq("gzh", toUserName)
                // 是否生效  （0否 1是）
                .eq("has_valid", SystemConst.ONE)
                // 回复类型  （0关注回复 1关键字回复）
                .eq("reply_type", SystemConst.ZERO));

        return replyTemplate(autoReplyBean, toUserName, fromUserName);
    }

    /**
     * 回复模板
     * @param autoReplyBean 自动回复配置
     * @param toUserName 开发者微信号
     * @param fromUserName 发送方帐号（一个OpenID）
     * @return 需要回复的消息
     */
    public Object replyTemplate(AutoReplyBean autoReplyBean, String toUserName, String fromUserName){
        // 该配置将要回复的消息写入缓存（有效期1天）
        String msg = stringRedisTemplate.opsForValue().get(RedisKeyConst.AUTO_REPLY_ID + autoReplyBean.getId());
        if(StrUtil.isNotBlank(msg)){
            return msg;
        }

        // 获取accessToken
        String accessToken = stringRedisTemplate.opsForValue().get(RedisKeyConst.ORIGINAL_ID + toUserName);
        if(autoReplyBean != null && StrUtil.isNotBlank(accessToken)) {
            Map map = new HashMap<String, String>(16);
            // 接收方帐号（收到的OpenID）  注意：接收和发送消息的 ToUserName、FromUserName是相反的
            map.put("ToUserName", fromUserName);
            // 开发者微信号
            map.put("FromUserName", toUserName);
            // 消息创建时间 （整型）
            map.put("CreateTime", DateUtil.getTimeStamp10());
            // 消息类型   text文本消息、image图片消息、news图文消息
            String messageType = autoReplyBean.getMessageType();
            map.put("MsgType", messageType);

            // ：：：回复文本消息
            if (WxCommonConst.MESSAGE_TEXT .equals(messageType)) {
                // 消息内容
                map.put("Content", autoReplyBean.getContent());
                // 该配置将要回复的消息写入缓存
                msg = XmlConvertUtil.mapToXml(map, SystemConst.XML, "","");
                stringRedisTemplate.opsForValue().set(RedisKeyConst.AUTO_REPLY_ID + autoReplyBean.getId(), msg,1, TimeUnit.DAYS);
                return msg;
            }

            // ：：：回复图片消息
            else if (WxCommonConst.MESSAGE_IMAGE .equals(messageType) && StrUtil.isNotBlank(autoReplyBean.getPicUrl())) {
                // 获取图片在本地路径
                String imgUrl = mediaUploadUtil.getImgUrl(autoReplyBean.getPicUrl(), MaterialImgUtil.LOCAL);
                if(StrUtil.isNotBlank(imgUrl)){
                    // 获取图文素材
                    String mediaId = MaterialImgUtil.uploadMaterial(WxCommonConst.MESSAGE_IMAGE, accessToken, imgUrl);
                    if(StrUtil.isNotBlank(mediaId)){
                        String formatStr = "<MediaId><![CDATA[%1$s]]></MediaId>";
                        String img = String.format(formatStr,mediaId);
                        // 图片
                        map.put("Image", img);
                        // 该配置将要回复的消息写入缓存
                        msg = XmlConvertUtil.mapToXml(map, SystemConst.XML, "","");
                        stringRedisTemplate.opsForValue().set(RedisKeyConst.AUTO_REPLY_ID + autoReplyBean.getId(), msg, 1, TimeUnit.DAYS);
                        return msg;
                    }
                }
            }

            // ：：：回复图文消息
            else if (WxCommonConst.MESSAGE_NEWS .equals(messageType)) {
                map.put("ArticleCount", "1");
                // 获取可以直接域名访问的图片链接
                String imgUrl = mediaUploadUtil.getImgUrl(autoReplyBean.getPicUrl(), MaterialImgUtil.DOMAIN);
                map.put("Articles", getNewsItem(autoReplyBean.getTitle(), autoReplyBean.getDescribe(), imgUrl, autoReplyBean.getUrl()));

                // 该配置将要回复的消息写入缓存
                msg = XmlConvertUtil.mapToXml(map, SystemConst.XML, "","");
                stringRedisTemplate.opsForValue().set(RedisKeyConst.AUTO_REPLY_ID + autoReplyBean.getId(), msg, 1, TimeUnit.DAYS);
                return msg;
            }
        }
        return "success";
    }

    /**
     * 获取图文项目
     * @param title  图文消息标题
     * @param description 图文消息描述
     * @param picUrl 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
     * @param url 点击图文消息跳转链接
     * @return
     */
    public String getNewsItem(String title, String description, String picUrl, String url){
        String formatStr = "<item>" +
                "<Title><![CDATA[%1$s]]></Title>" +
                "<Description><![CDATA[%2$s]]></Description>" +
                "<PicUrl><![CDATA[%3$s]]></PicUrl>" +
                "<Url><![CDATA[%4$s]]></Url>" +
                "</item>";
       return String.format(formatStr, title, description, picUrl, url);
    }
}

/** 图文请求栗子：
 * <xml>
 *   <ToUserName><![CDATA[toUser]]></ToUserName>
 *   <FromUserName><![CDATA[fromUser]]></FromUserName>
 *   <CreateTime>12345678</CreateTime>
 *   <MsgType><![CDATA[news]]></MsgType>
 *   <ArticleCount>1</ArticleCount>
 *   <Articles>
 *     <item>
 *       <Title><![CDATA[title1]]></Title>
 *       <Description><![CDATA[description1]]></Description>
 *       <PicUrl><![CDATA[picurl]]></PicUrl>
 *       <Url><![CDATA[url]]></Url>
 *     </item>
 *   </Articles>
 * </xml>
 */


