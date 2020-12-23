package com.wechat.common.define;


/**
 * redis key定义
 * @author dxf
 * @version 1.0
 * @date 2020/11/7 21:08
 */
public interface RedisKeyConst {
    /**
     * （前缀 + 文件标识 , 文件路径）
     */
    String FILE_ID = "fileId_";

    /**
     * （前缀 +  AppId ，accessToken）
     */
    String TOKEN = "token_";

    /**
     * （前缀 + 原始公众号 ， accessToken）
     */
    String ORIGINAL_ID = "originalId_";

    /**
     * （前缀 + 自动回复配置主键 ， 回复的消息）
     */
    String AUTO_REPLY_ID = "autoReplyId_";

    /**
     * 公众号、小程序配置信息相关
     */
    String GZH_APPID  = "gzhAppId";
    String GZH_APP_SECRET  = "gzhAppSecret";
    String XCX_APPID  = "xcxAppId";
    String XCX_APP_SECRET  = "xcxAppSecret";
    String PROJECT_NAME  = "projectName";

    /**
     * 用户信息(hash结构)
     * key : cardid 卡号
     *      hash_key: user_info 用户信息
     *      hash_key：balance 余额
     *      hash_key：integral 积分
     */
    String CARD_ID  = "cardid";
    String USER_INFO = "user_info";
    String BALANCE = "balance";
    String INTEGRAL = "integral";

    /**
     * 首页快捷入口配置    key: quickEntry_ + 项目id
     */
    String QUICK_ENTRY = "quickEntry_";

    /**
     * 首页轮播图片        key: slideShow_ + 项目id
     */
    String SLIDE_SHOW = "slideShow_";

    /**
     * 收货地址
     * key: shippingAddress + cardId
     *      hash_key: 收货地址主键
     */
    String SHIPPING_ADDRESS = "shipping_address_";
}
