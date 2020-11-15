package com.wechat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author dai
 * @Date 2020/10/31
 */
@MapperScan("com.wx.**.mapper")
@EnableScheduling //开启定时任务
@SpringBootApplication
public class thirdpartyApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(thirdpartyApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(thirdpartyApplication.class);
    }
}