package com.wechat;

import com.wechat.email.model.SoHuBean;
import com.wechat.email.service.SoHuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

/**
 * @Author dai
 * @Date 2020/10/31 
 */
@RunWith(SpringRunner.class)
// 因为business依赖了有主启动类的thirdparty，junit不知道加载哪个启动类，需要在junit的@SpringBootTest注解上指定启动类。
// 否则会报：junit报错Found multiple @SpringBootConfiguration annotated classes
@SpringBootTest(classes = {ServiceApplication.class})
public class SoHuTest {
    @Autowired
    private SoHuService soHuService;

    @Test
    public void test() throws Exception{
        SoHuBean soHuBean = new SoHuBean();
        // 邮件接收者的地址
        ArrayList<String> toAddress = new ArrayList<>();
        toAddress.add("18650218966@163.com");
        soHuBean.setToAddress(toAddress);

        //  邮件主题 (必)
        soHuBean.setSubject("零悦mall");
        //  邮件内容 (必)
        soHuBean.setContent("<a href=\"https://www.sohu.com/a/440603825_429139?code=f378fed293e6fdc04a380ccf90132043&amp;spm=smpc.home.top-news1.2.160895750201564vfqrg\" data-param=\"&amp;_f=index_cpc_1_0\" target=\"_blank\" title=\"总书记勉励青少年追求梦想\" data-spm-data=\"2\"><b>总书记勉励青少年追求梦想</b></a>");

        // 邮件格式 (必)
        soHuBean.setFormat(SoHuBean.FORMAT_HTML);

        // 邮件附件的文件名
        ArrayList<String> attachFileNames = new ArrayList<>();
        attachFileNames.add("E:\\dai\\MyBatis-Plus.md");
        soHuBean.setAttachFilePath(attachFileNames);

        soHuService.sendMail(soHuBean);
    }

}
