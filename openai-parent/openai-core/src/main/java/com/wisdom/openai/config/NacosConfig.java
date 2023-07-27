package com.wisdom.openai.config;

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
public class NacosConfig {
    @Value("${openai.baseUrl:https://api.openai.com/}")
    private String baseUrl;

    @Value("${openai.token:}")
    private String token;

    @Value("${openai.timeout:10L}")
    private Long timeout;
}
