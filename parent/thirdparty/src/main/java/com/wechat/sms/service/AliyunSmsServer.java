package com.wechat.sms.service;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.wechat.constant.SystemConst;
import com.wechat.tool.RedisUtil;
import com.wechat.tool.ValidatorUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 阿里云短信
 * @Author dai
 * @Date 2020/11/29
 * @version 1.0
 * 官网文档：https://help.aliyun.com/document_detail/112148.html?spm=a2c4g.11186623.6.656.3cbc152f6LjwGt
 */
@Service
public class AliyunSmsServer {
    private static Logger LOG = LoggerFactory.getLogger(AliyunSmsServer.class);

    /**
     * 开发者自己的AK (在阿里云访问控制台寻找：存储对象oss【https://oss.console.aliyun.com/overview】
     */
    @Value("${aliyun.access-key-id}")
    public String accessKeyId;
    @Value("${aliyun.access-key-secret}")
    public String accessKeySecret;
    /**
     * 短信签名
     */
    @Value("${aliyun.sign-name}")
    public String signName;

    /**
     * 验证码模板
     */
    @Value("${aliyun.code-templateI-id}")
    public String codeTemplateId;

    /**
     * 短信通知模板
     */
    @Value("${aliyun.message-template-id}")
    public String messageTemplateId;

    /**
     * 产品名称:云通信短信API产品,开发者无需替换
     */
    static final String product = "Dysmsapi";
    /**
     * 产品域名,开发者无需替换
     */
    static final String domain = "dysmsapi.aliyuncs.com";
    @Autowired
    private RedisUtil redisUtil;

    /*  案例
        Map<String,String> map = new HashMap<>();
        map.put("code","99999");参数
        //发短信
        Boolean bool =  aliyunServer.sendSms("18650218966", "SMS_182674403",0,map,200);
     */

    /**
     * 发送验证码，有效期5分钟
     * @param phoneNumber
     * @return
     */
    public boolean sendCode(String phoneNumber){
        // 校验手机格式是否正确
        boolean validPhone = ValidatorUtil.isValidPhone(phoneNumber);
        if(validPhone){
            HashMap<String, String> map = new HashMap<>(16);
            map.put("code", RandomUtil.randomNumbers(4));
            return send(phoneNumber, codeTemplateId, 0, map, 5);
        }
        LOG.error("手机号:{}, 校验结果为非手机格式", phoneNumber);
        return false;
    }

    /**
     * 发送短信
     * @param phoneNumbers 手机号
     * @param templateId 模板ID
     * @param type 模板类型  0：验证码模板  1：通知模板
     * @param params 参数
     * @param overtime 验证码有效期，值为0时不写入缓存, 单位：分
     * @return 是否发送成功
     */
    public boolean send(String phoneNumbers, String templateId, int type, Map<String,String> params, int overtime) {
        try {
            //可自助调整超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");

            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            //组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();
            //必填:待发送手机号
            request.setPhoneNumbers(phoneNumbers);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(signName);
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(templateId);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam(JSON.toJSONString(params));

            //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");

            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            request.setOutId("yourOutId");
            //hint 此处可能会抛出异常，注意catch
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if(sendSmsResponse == null){
                return false;
            }
            String message = sendSmsResponse.getMessage();
            if(SystemConst.OK.equals(sendSmsResponse.getCode()) ){
                // 验证码写入缓存
                if (0 == type){
                    //短信发送成功，写入缓存
                    if(overtime > 0){
                        redisUtil.set(phoneNumbers, params.get("code"), overtime, TimeUnit.MINUTES );
                    };
                }
                return true;
            }
            //短信发送失败
            LOG.info("手机号:{}，发送短信失败,原因:{}", phoneNumbers, message);
        }catch (Exception e){
            LOG.error("发送短信异常:::");
            LOG.error(ExceptionUtils.getStackTrace(e));
        }
        return false;
    }

    /**
     * 从缓存中获取短信验证码
     * @param phone 手机号
     * @return 验证码。如果验证码失效返回null
     */
    public String getCode(String phone){
        return redisUtil.get(phone);
    }

}
