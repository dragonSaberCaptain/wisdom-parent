package com.wisdom.gateway.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * TODO (用一句话描述该类的作用)
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/24 14:54 星期五
 */
@Slf4j
@Component
public class HttpPostBodyFilter implements GlobalFilter, Ordered {

    @Override
    public Mono filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (exchange.getRequest().getHeaders().getContentType() == null) {
            return chain.filter(exchange);
        } else {
            return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer ->
            {
                DataBufferUtils.retain(dataBuffer);
                Flux cachedFlux = Flux.defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
                ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                    @Override
                    public Flux getBody() {
                        return cachedFlux;
                    }
                };
                //exchange.getAttributes().put(CACHE_REQUEST_BODY_OBJECT_KEY, cachedFlux);
                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            });
        }
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
