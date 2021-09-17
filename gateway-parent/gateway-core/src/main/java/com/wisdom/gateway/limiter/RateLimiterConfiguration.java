package com.wisdom.gateway.limiter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * 网关限流器 ps:还没发测试是否有效
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/7 17:26 星期二
 */
@Slf4j
@Configuration
public class RateLimiterConfiguration {
    @Value("${resolver.header_key:token}")
    private String headerKey;

    @Value("${resolver.param_key:id}")
    private String paramKey;

    /**
     * IP限流 (通过exchange对象可以获取到请求信息，这边用了HostName)
     */
    @Bean
    @Primary
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }

    /**
     * 请求头限流 (通过exchange对象可以获取到请求信息，获取当前请求的用户 BASE_TOKEN)
     * 使用这种方式限流，请求Header中必须携带相应参数参数 常用token
     */
    @Bean
    public KeyResolver headerKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getHeaders().getFirst(headerKey));
    }


    /**
     * 接口限流 (获取请求地址的url作为限流key)
     */
    @Bean
    public KeyResolver urlKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }

    /**
     * 参数限流
     */
    @Bean
    public KeyResolver paramsKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst(paramKey));
    }
}