package com.wisdom.gateway.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * 网关统一拦截器
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/8/31 14:07 星期二
 */
@Slf4j
@Configuration
public class GatewayFilter implements GlobalFilter, Ordered {
    @Value("${common.params.token_key:token}")
    private String tokenKey;

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        ServerHttpResponse serverHttpResponse = exchange.getResponse();

        log.info("》》》》》》》》》》》》》》》》》》》》》开始记录请求信息");
        //Header参数
        HttpHeaders headers = serverHttpRequest.getHeaders();
        for (String key : headers.keySet()) {
            log.info("请求头参数：【{}:{}】", key, headers.get(key));
        }

        //URL请求参数
        MultiValueMap<String, String> map = serverHttpRequest.getQueryParams();
        for (String key : map.keySet()) {
            log.info("请求参数：【{}:{}】", key, map.get(key));
        }

        String reqPath = serverHttpRequest.getPath().pathWithinApplication().toString();
        log.info("请求网关转发地址:【{}】", serverHttpRequest.getPath());
        log.info("《《《《《《《《《《《《《《《《《《《《《请求信息记录结束");

        String substring = reqPath.substring(reqPath.lastIndexOf("/"));
        //登录相关全部放行
        if ("/login.html".equals(substring) || "/index.html".equals(substring)) {
            return chain.filter(exchange);
        }

        //swagger相关全部放行
        if (reqPath.contains("/swagger") || "/api-docs".equals(substring)) {
            return chain.filter(exchange);
        }
        //druid相关全部放行
        if (reqPath.contains("/druid")) {
            return chain.filter(exchange);
        }
        //验证token
//        String baseToken = serverHttpRequest.getHeaders().getFirst(tokenKey);
//        if (StringUtil.isBlank(baseToken)) {
//            return ResponseUtil.resultMsgToMono(ResultEnum.TOKEN_IS_EMPTY, serverHttpResponse);
//        }
        //给请求头添加签名
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
