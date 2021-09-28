package com.wisdom.gateway.exception;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.wisdom.config.enums.ResultEnum;
import com.wisdom.gateway.tools.response.ResponseUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * 自定义限流异常处理
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/28 10:18 星期二
 */
@Data
@Slf4j
public class SentinelFallbackHandler implements WebExceptionHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }
        if (!BlockException.isBlockException(ex)) {
            return Mono.error(ex);
        }
        return handleBlockedRequest(exchange, ex).flatMap(response -> writeResponse(exchange));
    }
    private Mono<Void> writeResponse(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        return ResponseUtil.resultMsgToMono(ResultEnum.BAN_REQUEST, response);
    }

    private Mono<ServerResponse> handleBlockedRequest(ServerWebExchange exchange, Throwable ex) {
        return GatewayCallbackManager.getBlockHandler().handleRequest(exchange, ex);
    }
}
