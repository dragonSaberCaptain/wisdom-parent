package com.wisdom.gateway.routes;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * nacos 动态路由
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/8/30 16:03 星期一
 */
@Slf4j
@Configuration
public class NacosDynamicRouteConfig implements ApplicationEventPublisherAware {
     @Value("${routes_info.data_id:routes.json}")
    private String dataId;

    @Value("${spring.application.name:gateway}")
    private String appName;

    @Value("${spring.cloud.nacos.config.file-extension:}")
    private String fileExtension;

    @Value("${spring.cloud.nacos.config.group:DEFAULT_GROUP}")
    private String group = "DEFAULT_GROUP";

    @Value("${spring.cloud.nacos.config.server-addr:Public}")
    private String serverAddr;

    @Value("${spring.cloud.nacos.config.namespace:properties}")
    private String serverNamespace;

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher applicationEventPublisher;

    private static final List<String> ROUTE_LIST = new ArrayList<>();

    @PostConstruct
    public void dynamicRouteByNacosListener() {

        try {
            Properties prop = new Properties();
            prop.put(PropertyKeyConst.NAMESPACE, serverNamespace);
            prop.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
            ConfigService config = NacosFactory.createConfigService(prop);
            String content = config.getConfig(dataId, group, 5000);
            publisher(content);
            config.addListener(dataId, group, new Listener() {
                @Override
                public void receiveConfigInfo(String config) {
                    publisher(config);
                }

                @Override
                public Executor getExecutor() {
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加路由
     */
    public Boolean addRoute(RouteDefinition def) {
        try {
            routeDefinitionWriter.save(Mono.just(def)).subscribe();
            ROUTE_LIST.add(def.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 删除路由
     */
    public Boolean clearRoute() {
        for (String id : ROUTE_LIST) {
            routeDefinitionWriter.delete(Mono.just(id)).subscribe();
        }
        ROUTE_LIST.clear();
        return false;
    }

    /**
     * 发布路由
     */
    private void publisher(String config) {
        clearRoute();
        try {
            log.info("--------------------------------------------【动态刷新路由信息】-----------------------------------");
            List<RouteDefinition> gateway = JSONObject.parseArray(config, RouteDefinition.class);
            for (RouteDefinition route : gateway) {
                addRoute(route);
            }
            applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this.routeDefinitionWriter));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher app) {
        applicationEventPublisher = app;
    }

}