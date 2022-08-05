package com.wisdom.gateway.config.swagger;

import com.wisdom.gateway.config.NacosConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * TODO (用一句话描述该类的作用)
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/2 15:47 星期四
 */

@Slf4j
@Primary
@Component
public class SwaggerProvider implements SwaggerResourcesProvider {
    @Autowired
    private NacosConfig nacosConfig;
    /**
     * 网关路由
     */
    @Autowired
    private RouteLocator routeLocator;

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routeHosts = new ArrayList<>();
        // 获取所有可用的host：serviceId
        routeLocator.getRoutes()
                .filter(route -> route.getUri().getHost() != null && !nacosConfig.getAppName().equals(route.getUri().getHost()))
                .subscribe(route -> routeHosts.add(route.getUri().getHost()));

        //去重
        List<String> routeHostsCollect = routeHosts.stream().distinct().collect(Collectors.toList());

        routeHostsCollect.forEach(instance -> {
            // 拼接url
            String url = "/" + instance.toLowerCase() + nacosConfig.getSwaggerApiDocs();
            SwaggerResource swaggerResource = new SwaggerResource();
            swaggerResource.setName(instance);
            swaggerResource.setUrl(url);
            swaggerResource.setSwaggerVersion(DocumentationType.OAS_30.getVersion());
            //先尝试获取swagger文档信息,不知道怎么判断nocos服务是否存活,暂时用此方法,以后在优化
//            String result = HttpUrlConnectionUtil.sendGetASyn("http://127.0.0.1" + url, null);
//            if (StringUtil.isNotBlank(result)) {
            //只生成请求成功的文档信息
            resources.add(swaggerResource);
//            }
        });
        Collections.sort(resources);
        return resources;
    }
}
