//package com.wisdom.gateway.config.swagger;
//
//import com.wisdom.gateway.config.NacosConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.context.annotation.Primary;
//import org.springframework.stereotype.Component;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.swagger.web.SwaggerResource;
//import springfox.documentation.swagger.web.SwaggerResourcesProvider;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
///**
// * Copyright © 2021 dragonSaberCaptain. All rights reserved.
// * <p>
// * 网关统一处理 Swagger (已废弃 代码保留)
// *
// * @author captain
// * @version 1.0
// * @datetime 2021/9/2 15:47 星期四
// */
//
//@Slf4j
//@Primary
//@Component
//public class SwaggerProvider implements SwaggerResourcesProvider {
//    @Autowired
//    private NacosConfig nacosConfig;
//    /**
//     * 网关路由
//     */
//    @Autowired
//    private RouteLocator routeLocator;
//
//    @Override
//    public List<SwaggerResource> get() {
//        List<SwaggerResource> resources = new ArrayList<>();
//        List<String> routeHosts = new ArrayList<>();
//        // 获取所有可用的host：serviceId
//        routeLocator.getRoutes()
//                .filter(route -> route.getUri().getHost() != null && !nacosConfig.getAppName().equals(route.getUri().getHost()))
//                .subscribe(route -> routeHosts.add(route.getUri().getHost()));
//
//        //去重
//        List<String> routeHostsCollect = routeHosts.stream().distinct().toList();
//
//        routeHostsCollect.forEach(instance -> {
//            // 拼接url
//            String url = "/" + instance.toLowerCase() + nacosConfig.getSwaggerApiDocs();
//            SwaggerResource swaggerResource = new SwaggerResource();
//            swaggerResource.setName(instance);
//            swaggerResource.setUrl(url);
//            swaggerResource.setSwaggerVersion(DocumentationType.OAS_30.getVersion());
//            resources.add(swaggerResource);
//        });
//        Collections.sort(resources);
//        return resources;
//    }
//}
