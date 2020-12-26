package com.wechat.email.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.wechat.email.config.SoHulAuthenticator;
import com.wechat.email.model.SoHuBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

@Service
/**
 * 搜狐邮箱
 * @Author dai
 * @Date 2020/10/31
 */
public class SoHuService {
    private static final Logger logger = LoggerFactory.getLogger(SoHuService.class);

    @Value("${sohu.host}")
    private String host;
    @Value("${sohu.port}")
    private String port;
    @Value("${sohu.validate}")
    private Boolean validate;
    @Value("${sohu.userName}")
    private String userName;
    @Value("${sohu.password}")
    private String passWord;
    @Value("${sohu.fromAddress}")
    private String fromAddress;

    /**
     * 发送邮件
     * @param mail 邮件内容
     * @return
     */
    public boolean sendMail(SoHuBean mail) {
        // 发送状态
        boolean sendStatus = false;
        // 判断是否需要身份认证
        SoHulAuthenticator authenticator = null;
        Properties pro = getProperties();
        if (validate) {
            // 如果需要身份认证，则创建一个密码验证器
            authenticator = new SoHulAuthenticator(userName, passWord);
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getInstance(pro, authenticator);
        sendMailSession.setDebug(false);
        try {
            // 根据session创建一个邮件消息
            MimeMessage mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(fromAddress);
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);

            // 设置邮件消息的主题
            mailMessage.setSubject(mail.getSubject(), "UTF-8");
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // 设置邮件消息的主要内容
            String format = mail.getFormat();
            if(StrUtil.isBlank(format)){
                logger.info("发送邮箱格式不能为空");
                return false;
            }
            // 以某种格式发送
            mailMessage.setContent(mail.getContent(), mail.getFormat());

            //是否有附件
            ArrayList<String> fileNames = mail.getAttachFilePath();
            if(ObjectUtil.isNotNull(fileNames)){
                // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
                Multipart mainPart = new MimeMultipart("mixed");
                // 设置邮件正文
                BodyPart content = new MimeBodyPart();
                content.setContent(mail.getContent(), SoHuBean.FORMAT_HTML);
                mainPart.addBodyPart(content);
                // 设置附件
                for (String fileName : fileNames) {
                    //邮件附件
                    BodyPart bodyPart = new MimeBodyPart();
                    FileDataSource source = new FileDataSource(fileName);
                    bodyPart.setDataHandler(new DataHandler(source));
                    //附件中文名乱码
                    bodyPart.setFileName(MimeUtility.encodeText(source.getName()));
                    mainPart.addBodyPart(bodyPart);
                }
                // 将MiniMultipart对象设置为邮件内容
                mailMessage.setContent(mainPart, SoHuBean.FORMAT_HTML);
            }

            // 创建邮件的接收者地址，并设置到邮件消息中
            ArrayList<String> toAddress = mail.getToAddress();
            for(String address : toAddress){
                Address to = new InternetAddress(address);
                mailMessage.setRecipient(Message.RecipientType.TO, to);
                // 发送邮件
                Transport.send(mailMessage);
            }
            sendStatus = true;
        } catch (Exception e) {
            e.printStackTrace();
            return sendStatus;
        }
        return sendStatus;
    }

    /**
     * 获得邮件会话属性
     */
    public Properties getProperties() {
        Properties p = new Properties();
        p.put("mail.smtp.host", host);
        p.put("mail.smtp.port", port);
        p.put("mail.smtp.auth", validate);
        return p;
    }
}
