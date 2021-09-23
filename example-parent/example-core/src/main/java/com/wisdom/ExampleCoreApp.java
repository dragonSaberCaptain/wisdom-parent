package com.wisdom;

import com.wisdom.tools.system.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

/**
 * Copyright © 2018 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @date 2018-07-25 17:58 星期三
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "com.wisdom.*")
@EnableDiscoveryClient //注册到注册中心
public class ExampleCoreApp {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ExampleCoreApp.class, args);
        SpringContextUtil.setApplicationContext(context);
        String startInfo = "--------------------------------------------【example-core service server started!】-----------------------------------";
        log.info(startInfo);
        System.out.println(startInfo);
    }
}
