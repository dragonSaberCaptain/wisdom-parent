package com.wisdom.gateway;

import com.wisdom.tools.system.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * 网关中心:跳转重定向，用户的验证登录，解决跨域，日志拦截，权限控制，限流，熔断，负载均衡，黑名单和白名单机制等
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/8/30 10:45 星期一
 */
@Slf4j
@SpringBootApplication(scanBasePackages="com.wisdom.*")
@EnableDiscoveryClient
public class GatewayCoreApp {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(GatewayCoreApp.class, args);
        SpringContextUtil.setApplicationContext(context);
        log.info("》》》》【 {} service server started !!! 】《《《《", GatewayCoreApp.class.getSimpleName());
    }
}
