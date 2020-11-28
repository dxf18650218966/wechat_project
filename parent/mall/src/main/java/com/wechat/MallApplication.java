package com.wechat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author dai
 * @Date 2020/10/31
 */
@MapperScan("com.wechat.**.mapper")
@EnableScheduling //开启定时任务
@SpringBootApplication
@EnableTransactionManagement //开启事务注解
public class MallApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MallApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MallApplication.class);
    }
}