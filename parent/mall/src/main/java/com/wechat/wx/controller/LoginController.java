package com.wechat.wx.controller;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.wechat.crypto.AESUtils;
import com.wechat.model.BusinessCode;
import com.wechat.model.ErrCode;
import com.wechat.model.ResponseModel;
import com.wechat.model.ResponseUtil;
import com.wechat.tool.RandomUtil;
import com.wechat.tool.RedisUtil;
import com.wechat.wx.entity.UserInfoBean;
import com.wechat.common.define.RedisKeyConst;
import com.wechat.wx.model.WxLoginInfo;
import com.wechat.wx.model.WxUserInfo;
import com.wechat.wx.service.LoginService;
import com.wechat.wx.service.WxInterfaceCallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Struct;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * 公众号、小程序注册登录
 * @author dxf
 * @date 2020/11/21 19:50
 * @version 1.0
 */
@RestController
@CrossOrigin
@RequestMapping("wx/login")
public class LoginController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private WxInterfaceCallService wxInterfaceCallServicel;
    @Autowired
    private LoginService loginService;

    /**
     * 小程序登录
     * 流程：
     *    前端：
     *         1：通过 wx.checkSession 判断会话秘钥 session_key 是否过期，如果过期重新调用此接口登录，并且把已经失效的session_token传递给后端清除
     *         2：首先通过 wx.login 获取 code 用户登录凭证（有效期五分钟）
     *         3：wx.getUserInfo 获取微信授权，得到用户信息和手机号，不包含 openid 等敏感信息
     *         4：调后台登录服务
     *    后端：
     *         1：通过 appid + appsecret + code 调用微信登录凭证，获取到 session_key（会话秘钥）、 openid（用户唯一标识）、unionid(用户唯一标识) 等
     *         2：获取手机号 (因为没有UnionID ，只能通过手机号关联)
     *         3：自定义登录状态 session_token，有效期为7天，
     *           由于微信是根据用户活跃度来续签 session_key 有效期，所以无法知道 session_key 的过期时间，只能在用户再次登录小程序时延长有效期
     *           之所以给session_token 设置有效期，是为避免用户登录之后，再也不登录了，导致 session_token 一直保存在缓存
     *          （session_token , session_key + openid） , session_token：UUID
     *         4: 添加用户数据: 已注册过的用户，只需要更新用户信息，返回session_token
     *
     * @param wxLoginInfo
     * @return
     */
    @PostMapping("small_program")
    public Object smallProgramLogin(@Validated @RequestBody WxLoginInfo wxLoginInfo){
        // 参数校验
        String projectId = wxLoginInfo.getProjectId();
        String code = wxLoginInfo.getCode();
        String encryptedData = wxLoginInfo.getEncryptedData();
        String iv = wxLoginInfo.getIv();
        String oldSessionToken = wxLoginInfo.getSession_token();
        WxUserInfo wxUserInfo = wxLoginInfo.getWxUserInfo();
        if(! StrUtil.isAllNotBlank( projectId, code, encryptedData, iv) || wxUserInfo == null){
            return ResponseUtil.returnFail( ErrCode.MISSING_REQUEST_PARAMETERS );
        }

        String appid = redisUtil.get(projectId, RedisKeyConst.XCX_APPID );
        String appSecret = redisUtil.get(projectId, RedisKeyConst.XCX_APP_SECRET );
        String projectName = redisUtil.get(projectId, RedisKeyConst.PROJECT_NAME );
        if(! StrUtil.isAllNotBlank(appid, appSecret, projectName)){
            return ResponseUtil.returnFail( ErrCode.REQUEST_PARAMETER_ERROR );
        }

        // 会话令牌， 如果有传入该值，说明session_key 已过期，前端需要重新登录，产生新的session_token，那么之前的session_token 需要清理掉
        if(StrUtil.isNotBlank(oldSessionToken)){
            redisUtil.del(oldSessionToken);
        }

        // 通过 appid + appsecret + code 调用微信登录凭证校验接口，获取到 session_key（会话秘钥） + openid(用户唯一标识)等
        ResponseModel responseModel = wxInterfaceCallServicel.jscode2session(appid, appSecret, code);
        if(! ResponseUtil.businessResult(responseModel)) {
            return responseModel;
        }
        JSONObject json = (JSONObject) JSONObject.toJSON(responseModel.getData());
        String openid = json.getString("openid");
        String sessionKey = json.getString("session_key");
        if(! StrUtil.isAllNotBlank(openid, sessionKey)){
            return ResponseUtil.returnFail( ErrCode.THIRD_PARTY_INTERFACE_ERR );
        }

        // 获取手机号
        String phoneNumber = loginService.getPhoneNumber(encryptedData, iv, sessionKey);
        if(StrUtil.isBlank(phoneNumber)){
            return ResponseUtil.returnFail( ErrCode.CELL_PHONE_NUMBER_IS_EMPTY );
        }

        // 用户信息
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setXcxOpenId(openid);
        userInfoBean.setPhone(phoneNumber);
        userInfoBean.setNickname(wxUserInfo.getNickName());
        userInfoBean.setHeadPortrait(wxUserInfo.getAvatarUrl());
        userInfoBean.setGender(wxUserInfo.getGender());

        // 通过手机号，判断用户是否已注册过  (清除用户信息缓存)
        Boolean res = false;
        String cardId = loginService.existsUserByPhone(projectId, phoneNumber);
        if(StrUtil.isNotBlank(cardId)){
            // 更新用户信息
            userInfoBean.setCardId(cardId);
            res = loginService.updateUserInfo(userInfoBean);
        }else{
            // 注册用户信息
            userInfoBean.setProject(projectId);
            userInfoBean.setProjectName(projectName);
            res = loginService.registeredUserInfo(userInfoBean);
        }

        // 登录成功
        if (res){
            // 自定义登录状态 session_token，有效期7天
            String value = sessionKey + openid;
            String sessionToken = RandomUtil.ensureNoRiskAtAll();
            redisUtil.set(sessionToken, value, 7, TimeUnit.DAYS);

            // 响应前端
            JSONObject response = new JSONObject();
            response.put("session_token", sessionToken);
            response.put("card_id", StrUtil.isNotBlank(cardId) ? cardId : userInfoBean.getCardId());
            return ResponseUtil.resultSuccess(response);
        }
        return ResponseUtil.returnFail(ErrCode.SYSTEM_INTERNAL_ERROR );
    }


    /**
     * 公众号登录
     * 流程：
     *    前端：1：用户同意授权，获取code
     *          详情说明：公众号网页授权后，会重定向到回调链接地址redirect_uri，并带上code，
     *          code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。
     *          栗子：如果用户同意授权，页面将跳转至: redirect_uri/?code=CODE&state=STATE。
     *    后端：2: 通过code换取网页授权access_token和openId
     *          3：通过access_token拉取会员信息
     *          4: 通过openId 判断用户是否已注册
     *                  已注册：更新会员
     *                  未注册：手机号必须传入（提示前端去注册页面）
     *          5：自定义登录状态 session_token，有效期为7天
     *
     */
    @PostMapping("mp_login")
    public Object mpLogin(@Validated @RequestBody HashMap<String,String> map){
        String projectId = map.get("project_id");
        String code = map.get("code");

        if(! StrUtil.isAllNotBlank( projectId, code)){
            return ResponseUtil.returnFail( ErrCode.MISSING_REQUEST_PARAMETERS );
        }

        String appid = redisUtil.get(projectId, RedisKeyConst.GZH_APPID );
        String appSecret = redisUtil.get(projectId, RedisKeyConst.GZH_APP_SECRET );
        String projectName = redisUtil.get(projectId, RedisKeyConst.PROJECT_NAME );
        if(! StrUtil.isAllNotBlank(appid, appSecret, projectName)){
            return ResponseUtil.returnFail( ErrCode.REQUEST_PARAMETER_ERROR );
        }

        // 通过code换取网页授权access_token和openId
        ResponseModel responseModel = wxInterfaceCallServicel.oauthAccessToken(appid, appSecret, code);
        if(! ResponseUtil.businessResult(responseModel)){
            return responseModel;
        }
        JSONObject accessJson = (JSONObject) JSONObject.toJSON(responseModel.getData());
        String accessToken = accessJson.getString("access_token");
        String openid = accessJson.getString("openid");
        if(! StrUtil.isAllNotBlank(openid, accessToken)){
            return ResponseUtil.returnFail( ErrCode.THIRD_PARTY_INTERFACE_ERR );
        }

        // 通过access_token拉取会员信息
        ResponseModel responseModel2 = wxInterfaceCallServicel.userInfo(openid, accessToken);
        if(! ResponseUtil.businessResult(responseModel2)){
            return responseModel2;
        }
        JSONObject userJson = (JSONObject) JSONObject.toJSON(responseModel2.getData());
        String nickname = userJson.getString("nickname");
        String sex = userJson.getString("sex");
        String headimgurl = userJson.getString("headimgurl");
        if(! StrUtil.isAllNotBlank(nickname, sex, headimgurl)){
            return ResponseUtil.returnFail( ErrCode.THIRD_PARTY_INTERFACE_ERR );
        }

        // 用户信息
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setGzhOpenId(openid);
        userInfoBean.setNickname(nickname);
        userInfoBean.setHeadPortrait(headimgurl);
        userInfoBean.setGender(sex);
        userInfoBean.setProject(projectId);
        userInfoBean.setProjectName(projectName);

        //  通过openId 判断用户是否已注册 (清除用户信息缓存)
        String cardId = loginService.existsUserByOpenId(openid);
        if(StrUtil.isBlank(cardId)){
            // 跳转注册页面
            JSONObject json = BusinessCode.json(BusinessCode.PLEASE_REGISTER );
            Console.log(JSONObject.toJSONString(userInfoBean));
            json.put("userInfo", AESUtils.encrypt(JSONObject.toJSONString(userInfoBean)));
            return ResponseUtil.resultSuccess(json);
        }

        // 更新用户信息
        userInfoBean.setCardId(cardId);
        Boolean res = loginService.updateUserInfo(userInfoBean);
        // 登录成功
        if (res){
            // 自定义登录状态 session_token，有效期7天
            String sessionToken = RandomUtil.ensureNoRiskAtAll();
            redisUtil.set(sessionToken, openid, 7, TimeUnit.DAYS );

            // 响应前端
            JSONObject response = new JSONObject();
            response.put("code","2001");
            response.put("session_token", sessionToken);
            response.put("card_id", userInfoBean.getCardId());
            return ResponseUtil.resultSuccess(response);
        }
        return ResponseUtil.returnFail(ErrCode.SYSTEM_INTERNAL_ERROR );
    }


    /**
     * 注册
     * @param map
     * @return
     */
    @PostMapping("mp_registered")
    public Object mpRegistered(@Validated @RequestBody HashMap<String,String> map){
        String userData = map.get("user_data");
        String phoneCipher = map.get("phone");

        if(! StrUtil.isAllNotBlank( userData, phoneCipher)){
            return ResponseUtil.returnFail( ErrCode.MISSING_REQUEST_PARAMETERS );
        }
        // 解密
        String phone = AESUtils.decrypt(phoneCipher);
        String userInfo = AESUtils.decrypt(userData);
        if(! StrUtil.isAllNotBlank( userData, phone)){
            return ResponseUtil.returnFail( ErrCode.REQUEST_PARAMETER_ERROR );
        }
        UserInfoBean userInfoBean = JSONObject.parseObject(userInfo, UserInfoBean.class);
        userInfoBean.setPhone(phone);


        // 通过手机号，判断用户是否已注册过  (清除用户信息缓存)
        Boolean res = false;
        String cardId = loginService.existsUserByPhone(userInfoBean.getProject(), phone);
        if(StrUtil.isNotBlank(cardId)){
            // 更新用户信息
            userInfoBean.setCardId(cardId);
            res = loginService.updateUserInfo(userInfoBean);
        }else{
            // 注册用户信息
            res = loginService.registeredUserInfo(userInfoBean);
        }
        if(res){
            // 自定义登录状态 session_token，有效期7天
            String sessionToken = RandomUtil.ensureNoRiskAtAll();
            redisUtil.set(sessionToken, userInfoBean.getGzhOpenId(), 7, TimeUnit.DAYS );

            // 响应前端
            JSONObject response = new JSONObject();
            response.put("session_token", sessionToken);
            response.put("card_id", userInfoBean.getCardId());
            return ResponseUtil.resultSuccess(response);
        }
        return ResponseUtil.returnFail( ErrCode.SYSTEM_INTERNAL_ERROR );
    }

    /**
     * 校验小程序sessionToken是否过期
     * @param map
     * @return true 没过期
     */
    @PostMapping("check_token")
    public Object checkToken(@Validated @RequestBody HashMap<String,String> map){
        String token = redisUtil.get(map.get("session_token"));
        return ResponseUtil.resultSuccess(StrUtil.isBlank(token) ? false : true);
    }
}
