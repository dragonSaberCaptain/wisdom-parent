package com.wisdom.gateway.config;

import com.wisdom.base.params.NacosCommonConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)
@Component
public class NacosConfig extends NacosCommonConfig {
    @Value("${routesInfo.dataId:}")
    private String dataId;
}
