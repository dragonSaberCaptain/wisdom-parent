package com.wisdom.gateway.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 重新包装post请求中的body
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
        if (null == exchange.getRequest().getHeaders().getContentType()) {
            return chain.filter(exchange);
        } else {
            return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer ->
            {
                DataBufferUtils.retain(dataBuffer);
                Flux<DataBuffer> cachedFlux = Flux.defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
                ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                    @Override
                    public Flux getBody() {
                        return cachedFlux;
                    }
                };
                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            });
        }
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
