package com.wechat.wx.model;

/**
 * 微信接口url
 * @author dxf
 * @version 1.0
 * @date 2020/11/6 22:23
 */
public interface WechatUrlConst {
    /**
     * access_token：基础接口调用凭据（系统）
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
     *     appid   	公众号的唯一标识（必）
     *     redirect_uri   授权后重定向的回调链接地址， 请使用 urlEncode 对链接进行处理（必）
     *     response_type   返回类型，请填写code（必）
     *     scope  应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且， 即使在未关注的情况下，只要用户授权，也能获取其信息 ）（必）
     *     state   重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节 (非必填)
     *     #wechat_redirect  无论直接打开还是做页面302重定向时候，必须带此参数 （必）
     */
    String WECHAT_AUTHORIZATION = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%1$s&redirect_uri=%2$s&response_type=code&scope=snsapi_userinfo&state=%1$s#wechat_redirect";

    /**
     * access_token：网页授权接口调用凭证（用户）
     */
    String OAUTH_ACCESS_TOKEN= " https://api.weixin.qq.com/sns/oauth2/access_token";

    /**
     * 公众号拉取用户信息
     */
    String USER_INFO = "https://api.weixin.qq.com/sns/userinfo";

    /**
     * 创建公众号菜单
     */
    String CREATE_MENU = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%1$s";

    /**
     * 登录凭证校验
     */
    String JSCODE_2_SESSION = "https://api.weixin.qq.com/sns/jscode2session";

}
