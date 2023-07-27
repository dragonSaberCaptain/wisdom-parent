package com.wisdom.base.params;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Copyright © 2022 dragonSaberCaptain. All rights reserved.
 * 获取nacos配置
 *
 * @author captain
 * @version 1.0
 * @datetime 2022/8/2 11:20 星期二
 */
@Data
@Accessors(chain = true)
@Component
public class NacosCommonConfig {
    @Value("${spring.application.name:}")
    private String appName;
    @Value("${spring.profiles.active:}")
    private String appActive;
    @Value("${spring.cloud.nacos.config.file-extension:}")
    private String fileExtension;
    @Value("${spring.cloud.nacos.config.group:}")
    private String group;
    @Value("${spring.cloud.nacos.config.server-addr:}")
    private String serverAddr;
    @Value("${spring.cloud.nacos.config.namespace:}")
    private String serverNamespace;

    @Value(value = "${common.params.token_key:}")
    private String tokenKey;
    @Value(value = "${common.params.swagger_api_docs:}")
    private String swaggerApiDocs;
    @Value(value = "${common.params.saltKey:}")
    private String saltKey;
    @Value(value = "${common.params.salt:}")
    private String salt;
}
