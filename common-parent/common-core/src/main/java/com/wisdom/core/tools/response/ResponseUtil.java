package com.wisdom.core.tools.response;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.wisdom.base.dto.ResultDto;
import com.wisdom.base.enums.EnumDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * 响应相关工具类
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/16 11:02 星期四
 */
@Slf4j
public class ResponseUtil {
    /**
     * QuoteFieldNames———-输出key时是否使用双引号,默认为true
     * WriteMapNullValue——–是否输出值为null的字段,默认为false
     * WriteNullNumberAsZero—-数值字段如果为null,输出为0,而非null
     * WriteNullListAsEmpty—–List字段如果为null,输出为[],而非null
     * WriteNullStringAsEmpty—字符类型字段如果为null,输出为"",而非null
     * WriteNullBooleanAsFalse–Boolean字段如果为null,输出为false,而非nul
     */
    public static Mono<Void> resultMsgToMono(EnumDao enumDao, ServerHttpResponse response) {
        ResultDto<Object> resultDto = new ResultDto<>(enumDao);
        String resultJson = JSON.toJSONString(resultDto, JSONWriter.Feature.WriteMapNullValue);
        DataBuffer buffer = response.bufferFactory().wrap(resultJson.getBytes(StandardCharsets.UTF_8));
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    public static Mono<Void> resultMsgToMono(String key, String value, ServerHttpResponse response) {
        ResultDto resultDto = new ResultDto(key, value);
        String resultJson = JSON.toJSONString(resultDto, JSONWriter.Feature.WriteMapNullValue);
        DataBuffer buffer = response.bufferFactory().wrap(resultJson.getBytes(StandardCharsets.UTF_8));
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    /**
     * 给输出流设置响应头参数
     *
     * @param fullFileName 输出流名称
     * @return java.io.OutputStream
     * @author created by captain on 2021-07-22 10:30:32
     */
    public static OutputStream getOutputStream(String fullFileName, HttpServletResponse response) {
        //设置response的属性
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/msexcel");
        response.addHeader("Access-Control-Allow-Origin", "*");
        OutputStream outputStream = null;
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fullFileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream;
    }
}
