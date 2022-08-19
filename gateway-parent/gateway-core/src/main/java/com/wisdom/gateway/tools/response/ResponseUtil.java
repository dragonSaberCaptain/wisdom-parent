package com.wisdom.gateway.tools.response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wisdom.config.dto.ResultDto;
import com.wisdom.config.enums.EnumDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * 组织响应
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/16 11:02 星期四
 */
@Slf4j
public class ResponseUtil {
    /**
     * SerializerFeature.QuoteFieldNames———-输出key时是否使用双引号,默认为true
     * SerializerFeature.WriteMapNullValue——–是否输出值为null的字段,默认为false
     * SerializerFeature.WriteNullNumberAsZero—-数值字段如果为null,输出为0,而非null
     * SerializerFeature.WriteNullListAsEmpty—–List字段如果为null,输出为[],而非null
     * SerializerFeature.WriteNullStringAsEmpty—字符类型字段如果为null,输出为"",而非null
     * SerializerFeature.WriteNullBooleanAsFalse–Boolean字段如果为null,输出为false,而非nul
     */
    public static Mono<Void> resultMsgToMono(String code, String msg, String subMsg, ServerHttpResponse response) {
        var resultDto = new ResultDto<>(code, msg, subMsg);
        var resultJson = JSON.toJSONString(resultDto, SerializerFeature.WriteMapNullValue);
        var buffer = response.bufferFactory().wrap(resultJson.getBytes(StandardCharsets.UTF_8));
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    public static Mono<Void> resultMsgToMono(String code, String msg, ServerHttpResponse response) {
        return resultMsgToMono(code, msg, null, response);
    }

    public static Mono<Void> resultMsgToMono(EnumDao enumDao, ServerHttpResponse response) {
        return resultMsgToMono(enumDao.getCode(), enumDao.getMsg(), enumDao.getSubMsg(), response);
    }

}
