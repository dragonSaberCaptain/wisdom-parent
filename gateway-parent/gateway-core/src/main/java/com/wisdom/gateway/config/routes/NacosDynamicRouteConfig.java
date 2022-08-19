package com.wisdom.gateway.config.routes;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.listener.Listener;
import com.wisdom.gateway.config.NacosConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
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
    @Autowired
    private NacosConfig nacosConfig;

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher applicationEventPublisher;

    private static final List<String> ROUTE_LIST = new ArrayList<>();

    @PostConstruct
    public void dynamicRouteByNacosListener() {
        try {
            var prop = new Properties();
            prop.put(PropertyKeyConst.NAMESPACE, nacosConfig.getServerNamespace());
            prop.put(PropertyKeyConst.SERVER_ADDR, nacosConfig.getServerAddr());

            var config = NacosFactory.createConfigService(prop);
            var content = config.getConfig(nacosConfig.getDataId(), nacosConfig.getGroup(), 5000);
            publisher(content);
            config.addListener(nacosConfig.getDataId(), nacosConfig.getGroup(), new Listener() {
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
    public void addRoute(RouteDefinition def) {
        try {
            routeDefinitionWriter.save(Mono.just(def)).subscribe();
            ROUTE_LIST.add(def.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除路由
     */
    public void clearRoute() {
        for (var id : ROUTE_LIST) {
            routeDefinitionWriter.delete(Mono.just(id)).subscribe();
        }
        ROUTE_LIST.clear();
    }

    /**
     * 发布路由
     */
    private void publisher(String config) {
        clearRoute();
        try {
            var gateway = JSONObject.parseArray(config, RouteDefinition.class);
            for (var route : gateway) {
                addRoute(route);
            }
            log.info("已动态刷新路由信息 !!!");
            log.info("resetRoutes:" + JSONObject.toJSONString(gateway));
            applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this.routeDefinitionWriter));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setApplicationEventPublisher(@Nonnull ApplicationEventPublisher app) {
        this.applicationEventPublisher = app;
    }

}