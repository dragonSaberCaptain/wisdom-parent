package com.wisdom.gateway.exception;

import com.wisdom.base.enums.ResultEnum;
import com.wisdom.base.exception.ResultException;
import com.wisdom.gateway.tools.response.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * 网关全局异常拦截器
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/23 18:02 星期四
 */

@Slf4j
@Order()
@Component
public class GlobalErrorWebExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        if (ex instanceof ResultException e1) {
            log.error("【返回结果异常】", e1.fillInStackTrace());
            return ResponseUtil.resultMsgToMono(e1.getCode(), e1.getMsg(), response);
        }

        log.error("【服务器内部异常】", ex);
        return ResponseUtil.resultMsgToMono(ResultEnum.RESULT_ENUM_9999, response);
    }
}