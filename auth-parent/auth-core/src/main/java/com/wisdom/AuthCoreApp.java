package com.wisdom;

import com.wisdom.tools.system.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * 认证中心:登录认证,鉴权等
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/8/30 10:45 星期一
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "com.wisdom.*", exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
public class AuthCoreApp {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(AuthCoreApp.class, args);
        Environment environment = context.getBean(Environment.class);
        String port = environment.getProperty("local.server.port");
        SpringContextUtil.setApplicationContext(context);
        log.info("》》》》【 {} : {} service started !!! 】《《《《", AuthCoreApp.class.getSimpleName(), port);
    }
}
