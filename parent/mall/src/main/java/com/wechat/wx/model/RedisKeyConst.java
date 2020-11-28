package com.wechat.wx.model;

/**
 * redis key前缀
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
    String GZH_APPID = "gzhAppId";
    String GZH_APP_SECRET = "gzhAppSecret";
    String XCX_APPID = "xcxAppId";
    String XCX_APP_SECRET = "xcxAppSecret";
    String PROJECT_NAME = "projectName";
}
