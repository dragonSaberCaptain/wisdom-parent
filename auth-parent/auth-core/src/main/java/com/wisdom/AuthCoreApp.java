package com.wisdom;

import com.wisdom.config.dto.SystemInfoDto;
import com.wisdom.config.enums.DateTimeEnum;
import com.wisdom.tools.datetime.DateUtilByZoned;
import com.wisdom.tools.system.SpringContextUtil;
import com.wisdom.tools.system.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

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
        MDC.put("BIZ_ID", DateUtilByZoned.getDateTime(DateTimeEnum.DATETIME_PATTERN_MILLI_UN));
        ApplicationContext context = SpringApplication.run(AuthCoreApp.class, args);
        SpringContextUtil.setApplicationContext(context);
        SystemInfoDto systemInfoDto = SystemUtil.printSystemInfo(AuthCoreApp.class);
        String startUpInfo = systemInfoDto.getSimpleName() + " service start on port:" + systemInfoDto.getPort() + " successful !!!";
        log.info(startUpInfo);
        System.out.println(startUpInfo);
    }
}
