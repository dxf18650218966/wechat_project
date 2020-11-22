package com.wechat.wx.controller;

import cn.hutool.core.util.StrUtil;
import com.wechat.constant.SystemConst;
import com.wechat.model.ErrCode;
import com.wechat.model.ResponseModel;
import com.wechat.model.ResponseUtil;
import com.wechat.tool.RedisUtil;
import com.wechat.wx.model.WxLoginInfo;
import com.wechat.wx.model.WxUserInfo;
import com.wechat.wx.service.WxInterfaceCallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 公众号、小程序注册登录
 * @author dxf
 * @date 2020/11/21 19:50
 * @version 1.0
 */
@RestController
@CrossOrigin
@RequestMapping("login")
public class LoginController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private WxInterfaceCallService wxInterfaceCallServicel;

    /**
     * 小程序登录
     * 流程：
     *    前端：
     *         1：通过 wx.checkSession 判断 session_key 是否过期，如果过期重新调用此接口注册登录，并且把已经失效的session_token传递给后端，用于清理
     *         2：首先通过 wx.login 获取 code 用户登录凭证（有效期五分钟）
     *         3：调用 wx.getUserInfo 获取微信授权，得到UserInfo用户信息对象，不包含 openid 等敏感信息
     *         4：将获取到的数据传给小程序登录接口（small_program_login）
     *    后端：
     *         1：通过 appid + appsecret + code 调用微信登录凭证校验接口，获取到 session_key（会话秘钥） + openid等
     *         2：自定义登录状态 session_token，有效期7天，
     *           由于微信是根据用户活跃度来续签 session_key 有效期，所以无法知道 session_key 的过期时间，只能在初始化首页时，有效期再续上7天
     *           之所以给session_token 设置有效期，是为避免用户登录之后，再也不登录了，导致session_token 一直在缓存
     *          （session_token , session_key + openid） , session_token = MD5(session_key + openid)
     *         3: 添加用户数据
     *
     * @param wxLoginInfo
     * @return
     */
    @PostMapping("small_program_login")
    public Object smallProgramLogin(@RequestBody WxLoginInfo wxLoginInfo){
        // :: 参数校验
        String appid = wxLoginInfo.getAppid();
        String code = wxLoginInfo.getCode();
        String encryptedData = wxLoginInfo.getEncryptedData();
        String iv = wxLoginInfo.getIv();
        String rawData = wxLoginInfo.getRawData();
        String signature = wxLoginInfo.getSignature();
        String sessionToken = wxLoginInfo.getSession_token();
        WxUserInfo wxUserInfo = wxLoginInfo.getWxUserInfo();
        if(! StrUtil.isAllNotBlank( appid, code, encryptedData, iv, rawData, signature) || wxUserInfo == null){
            return ResponseUtil.returnFail( ErrCode.MISSING_REQUEST_PARAMETERS );
        }

        String appSecret = redisUtil.get(appid);
        if(StrUtil.isBlank(appSecret)){
            return ResponseUtil.returnFail( ErrCode.REQUEST_PARAMETER_ERROR );
        }

        // 会话令牌， 如果有传入该值，说明session_key 已过期，前端需要重新登录，那会产生新的session_token，那么之前的session_token 需要清理掉
        redisUtil.del(sessionToken);

        // 通过 appid + appsecret + code 调用微信登录凭证校验接口，获取到 session_key（会话秘钥） + openid等
        ResponseModel responseModel = wxInterfaceCallServicel.jscode2session(appid, appSecret, code);
        // 通讯和业务通成功情况下
        if(SystemConst.SUCCESS .equals(responseModel.getResultCode()) && SystemConst.SUCCESS .equals(responseModel.getResultCode())){
            responseModel.getData();
        }else{
            return responseModel;
        }


        return null;
    }
}
