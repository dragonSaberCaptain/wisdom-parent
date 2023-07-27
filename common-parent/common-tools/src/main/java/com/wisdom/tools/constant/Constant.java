package com.wisdom.tools.constant;

import com.wisdom.tools.system.SpringContextUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * Copyright © 2022 dragonSaberCaptain. All rights reserved.
 * 系统全局通用参数
 *
 * @author captain
 * @version 1.0
 * @datetime 2022/8/23 10:31 星期二
 */
@Slf4j
@Data
@Accessors(chain = true)
public class Constant {
    public static final String BIZ_ID = "BIZ_ID";

    public static final String SPRING_APPLICATION_NAME = SpringContextUtil.getEnvironmentProperty("`spring.application.name`");
    public static final String SERVER_PORT = SpringContextUtil.getEnvironmentProperty("server.port");
    public static final String SPRING_PROFILES_ACTIVE = SpringContextUtil.getEnvironmentProperty("spring.profiles.active");
}
