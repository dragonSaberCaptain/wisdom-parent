/*
 * Copyright (c) 2023 dragonSaberCaptain.All rights reserved.
 * 版权创建时间:2023/7/28 下午5:38
 * 当前项目名:wisdom-parent
 * 当前模块名:example-core
 * 当前文件的权限定名:com.wisdom.ExampleCoreApp
 * 当前文件的名称:ExampleCoreApp.java
 * 当前文件的类名:ExampleCoreApp
 * 上一次文件修改的日期时间:2023/7/28 下午5:38
 *
 */

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
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@EnableAsync
@EnableDiscoveryClient //注册到注册中心
@EnableTransactionManagement //开启事务
@SpringBootApplication(scanBasePackages = "com.wisdom.*", exclude = {DataSourceAutoConfiguration.class})
public class ExampleCoreApp {
    public static void main(String[] args) {
        MDC.put(Constant.BIZ_ID, DateUtilByZoned.getDateTime(DateTimeEnum.DATETIME_PATTERN_MILLI_UN));
        ApplicationContext context = SpringApplication.run(ExampleCoreApp.class, args);
        SpringContextUtil.setApplicationContext(context);
        SystemInfoDto systemInfoDto = SystemUtil.printSystemInfo(ExampleCoreApp.class);
        String startUpInfo = systemInfoDto.getSimpleName() + " service start on port:" + systemInfoDto.getPort() + " successful !!!";
        System.out.println(startUpInfo);
        System.out.println("test");
        log.info(startUpInfo);
    }
}
