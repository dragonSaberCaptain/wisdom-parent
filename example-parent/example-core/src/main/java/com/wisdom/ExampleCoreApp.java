package com.wisdom;

import com.wisdom.tools.system.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Copyright © 2018 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @date 2018-07-25 17:58 星期三
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "com.wisdom.*", exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient //注册到注册中心
@EnableTransactionManagement //开启事务
public class ExampleCoreApp {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ExampleCoreApp.class, args);
        Environment environment = context.getBean(Environment.class);
        String port = environment.getProperty("local.server.port");
        SpringContextUtil.setApplicationContext(context);
        log.info("》》》》【 {} : {} service started !!! 】《《《《", ExampleCoreApp.class.getSimpleName(), port);
    }
}
