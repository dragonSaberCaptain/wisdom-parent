package com.wisdom.gateway.adapter;

import com.wisdom.base.enums.ConstantEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
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
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (ConstantEnum.POST.getCode().equalsIgnoreCase(exchange.getRequest().getMethodValue())) {
            //当body中没有缓存时，只会执行这一个拦截器， 原因是fileMap中的代码没有执行，所以需要在body为空时构建一个空的缓存
            DefaultDataBufferFactory defaultDataBufferFactory = new DefaultDataBufferFactory();
            DefaultDataBuffer defaultDataBuffer = defaultDataBufferFactory.allocateBuffer(0);
            //构建新数据流， 当body为空时，构建空流
            Flux<DataBuffer> bodyDataBuffer = exchange.getRequest().getBody().defaultIfEmpty(defaultDataBuffer);
            return DataBufferUtils.join(bodyDataBuffer).flatMap(dataBuffer -> {
                DataBufferUtils.retain(dataBuffer);
                Flux<DataBuffer> cachedFlux = Flux.defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
                ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                    @Override
                    public Flux<DataBuffer> getBody() {
                        return cachedFlux;
                    }
                };
                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            });
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
