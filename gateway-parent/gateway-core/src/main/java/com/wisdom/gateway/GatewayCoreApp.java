package com.wisdom.gateway;

import com.wisdom.tools.system.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * 网关中心:简单的跳转重定向，用户的验证登录，解决跨域，日志拦截，权限控制，限流，熔断，负载均衡，黑名单和白名单机制等
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/8/30 10:45 星期一
 */
@SpringBootApplication(scanBasePackages="com.wisdom.*")
@EnableDiscoveryClient
public class GatewayCoreApp {
    private static final Logger logger = LoggerFactory.getLogger(GatewayCoreApp.class);

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(GatewayCoreApp.class, args);
        SpringContextUtil.setApplicationContext(context);
        String startInfo = "--------------------------------------------【gateway-core service server started!】-----------------------------------";
        logger.info(startInfo);
        System.out.println(startInfo);

    }
}
