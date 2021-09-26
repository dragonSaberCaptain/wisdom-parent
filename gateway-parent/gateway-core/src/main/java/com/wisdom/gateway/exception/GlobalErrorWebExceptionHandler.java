package com.wisdom.gateway.exception;

import com.wisdom.config.dto.ResultDto;
import com.wisdom.config.enums.HttpEnum;
import com.wisdom.config.enums.ResultEnum;
import com.wisdom.config.exception.ResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * TODO (用一句话描述该类的作用)
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/23 18:02 星期四
 */

@Slf4j
@Order(-2)
@Component
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes,
                                          ResourceProperties resourceProperties,
                                          ServerCodecConfigurer serverCodecConfigurer,
                                          ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, applicationContext);
        this.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(
                RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(
            ServerRequest request) {
        return ServerResponse.status(getHttpStatus(request))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(getErrorResponse(request)));
    }

    private ResultDto getErrorResponse(ServerRequest request) {
        Map<String, Object> errorPropertiesMap = getErrorAttributes(request,
                ErrorAttributeOptions.of(ErrorAttributeOptions.Include.BINDING_ERRORS,
                        ErrorAttributeOptions.Include.EXCEPTION,
                        ErrorAttributeOptions.Include.STACK_TRACE,
                        ErrorAttributeOptions.Include.MESSAGE));
        String exception = errorPropertiesMap.get("exception").toString();

        if (ResultException.class.getName().equals(exception)) {
            log.error("【错误网关】【{}】", errorPropertiesMap);
            return new ResultDto(HttpEnum.BAD_GATEWAY);
        }

        if (RedisConnectionFailureException.class.getName().equals(exception)) {
            log.error("【缓存异常】【{}】", errorPropertiesMap);
            return new ResultDto(ResultEnum.REDIS_NO_OPEN);
        }
        if (NotFoundException.class.getName().equals(exception)) {
            log.error("【服务器不可用】【{}】", errorPropertiesMap);
            return new ResultDto(HttpEnum.SERVICE_UNAVAILABLE);
        }

        log.error("【服务器内部异常】【{}】", errorPropertiesMap);
        return new ResultDto(ResultEnum.SERVER_INTERNAL_EXCEPTION);
    }

    private HttpStatus getHttpStatus(ServerRequest request) {
        Map<String, Object> errorPropertiesMap = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        int status = (int) errorPropertiesMap.get("status");
        return HttpStatus.valueOf(status);
    }
}