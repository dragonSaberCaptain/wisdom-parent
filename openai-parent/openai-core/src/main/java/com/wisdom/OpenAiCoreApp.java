package com.wisdom;

import com.wisdom.base.dto.SystemInfoDto;
import com.wisdom.base.enums.DateTimeEnum;
import com.wisdom.tools.constant.Constant;
import com.wisdom.tools.datetime.DateUtilByZoned;
import com.wisdom.tools.system.SpringContextUtil;
import com.wisdom.tools.system.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Copyright © 2018 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @dateTime 2018-07-25 17:58 星期三
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "com.wisdom.**", exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient //注册到注册中心
@EnableTransactionManagement //开启事务
public class OpenAiCoreApp {
    public static void main(String[] args) {
        MDC.put(Constant.BIZ_ID, DateUtilByZoned.getDateTime(DateTimeEnum.DATETIME_PATTERN_MILLI_UN));
        ApplicationContext context = SpringApplication.run(OpenAiCoreApp.class, args);
        SpringContextUtil.setApplicationContext(context);
        SystemInfoDto systemInfoDto = SystemUtil.printSystemInfo(OpenAiCoreApp.class);
        String startUpInfo = systemInfoDto.getSimpleName() + " service start on port:" + systemInfoDto.getPort() + " successful !!!";
        System.out.println(startUpInfo);
        log.info(startUpInfo);
    }
}
