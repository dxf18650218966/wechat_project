package com.wechat.wx.model;

/**
 * 微信接口url
 * @author dxf
 * @version 1.0
 * @date 2020/11/6 22:23
 */
public interface WechatUrlConst {
    /**
     * 获取 Access token
     */
    String ACCESS_TOKEN  = "https://api.weixin.qq.com/cgi-bin/token";

    /**
     * 新增临时素材
     * 媒体文件在微信后台保存时间为3天，即3天后media_id失效。
     */
    String MEDIA_UPLOAD  = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=%1$s&type=%2$s";

    /**
     * 微信的公众平台域名
     */
    String PLATFORM_DOMAIN_NAME =  "http://mp.weixin.qq.com";

    /**
     * 公众号网页授权
     */
    String WECHAT_AUTHORIZATION = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%1$s&redirect_uri=%2$s&response_type=code&scope=snsapi_userinfo&state=%1$s#wechat_redirect";

    /**
     * 创建公众号菜单
     */
    String CREATE_MENU = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%1$s";

    /**
     * 登录凭证校验
     */
    String JSCODE_2_SESSION = "https://api.weixin.qq.com/sns/jscode2session";

}
