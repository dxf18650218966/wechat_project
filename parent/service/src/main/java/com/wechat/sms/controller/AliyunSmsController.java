package com.wechat.sms.controller;

import cn.hutool.core.util.StrUtil;
import com.wechat.constant.BusinessMsgConst;
import com.wechat.crypto.AESUtils;
import com.wechat.model.ErrCode;
import com.wechat.model.ResponseUtil;
import com.wechat.sms.service.AliyunSmsServer;
import com.wechat.tool.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/** 阿里短信服务
 * @Author dai
 * @Date 2020/11/29 
 */
@RestController
@CrossOrigin
@RequestMapping("aliyun_sms")
public class AliyunSmsController {
    @Autowired
    private AliyunSmsServer aliyunSmsServer;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 发送短信验证码
     */
    @PostMapping("send_code")
    public Object sendCode(@RequestBody HashMap<String,String> requestMap){
        String phoneCipher = requestMap.get("phone");
        if(! StrUtil.isAllNotBlank(phoneCipher)){
            return ResponseUtil.returnFail( ErrCode.MISSING_REQUEST_PARAMETERS );
        }
        // 解密获取手机号
        String phone = AESUtils.decrypt(phoneCipher);
        if(! StrUtil.isAllNotBlank(phone)){
            return ResponseUtil.returnFail( ErrCode.REQUEST_PARAMETER_ERROR );
        }

        // 发送短信
        boolean res = aliyunSmsServer.sendCode(phone);
        if(res){
            return ResponseUtil.resultSuccess( BusinessMsgConst.SEND_SUCCESS );
        }
        return ResponseUtil.resultFail( ErrCode.THIRD_PARTY_INTERFACE_ERR );
    }

    /**
     * 校验验证码
     */
    @PostMapping("validation_code")
    public Object validationCode(@RequestBody HashMap<String,String> requestMap){
        String phone = requestMap.get("phone");
        String code = requestMap.get("code");
        if(! StrUtil.isAllNotBlank(phone, code)){
            return ResponseUtil.returnFail( ErrCode.MISSING_REQUEST_PARAMETERS );
        }
        if(code.equals(redisUtil.get(phone))){
            return ResponseUtil.resultSuccess(BusinessMsgConst.CHECK_SUCCESS);
        }
        return ResponseUtil.resultFail(ErrCode.VERIFY_COD_ERROR );
    }
}
