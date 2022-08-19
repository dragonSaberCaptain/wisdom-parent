package com.wisdom.gateway.adapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.wisdom.config.dto.HttpModelDto;
import com.wisdom.config.enums.ConstantEnum;
import com.wisdom.config.enums.DateTimeEnum;
import com.wisdom.config.enums.NumberEnum;
import com.wisdom.config.enums.ResultEnum;
import com.wisdom.gateway.config.NacosConfig;
import com.wisdom.gateway.tools.request.RequestUtil;
import com.wisdom.gateway.tools.response.ResponseUtil;
import com.wisdom.tools.certificate.asymmetric.AsymmetricModel;
import com.wisdom.tools.certificate.asymmetric.AsymmetricUtil;
import com.wisdom.tools.certificate.asymmetric.MyKeyPair;
import com.wisdom.tools.database.RedisDao;
import com.wisdom.tools.datetime.DateUtilByZoned;
import com.wisdom.tools.string.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

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
@Component
public class GatewayFilter implements GlobalFilter, Ordered {
    @Autowired
    private RedisDao redisDao;

    @Autowired
    private NacosConfig nacosConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("网关处理开始");
        var request = exchange.getRequest();
        var response = exchange.getResponse();

        //通用请求数据对象
        var requestInfoModel = getRequestInfoModel(request);
        var requestInfoModelJson = JSONObject.toJSONString(requestInfoModel);
        log.info("》通用请求记录开始【{}】通用请求记录结束《", requestInfoModelJson);

        //判断不需要验证的,放行
        var roadblock = roadblock(requestInfoModel.getUrl());
        if (roadblock) {
            return chain.filter(exchange);
        }

        var headers = request.getHeaders();
        if (headers.isEmpty()) {
            return ResponseUtil.resultMsgToMono(ResultEnum.RESULT_ENUM_1012, response);
        }

        //验证token
        var token = headers.getFirst(nacosConfig.getTokenKey());
        if (StringUtil.isBlank(token)) {
            return ResponseUtil.resultMsgToMono(ResultEnum.RESULT_ENUM_1008, response);
        }

        //设置网关盐值信息
        var systemSalt = redisDao.save(nacosConfig.getSaltKey(), DateUtilByZoned.getDateTime(DateTimeEnum.DATETIME_PATTERN_MILLI_UN), NumberEnum.SEVEN.getCodeToInt(), TimeUnit.DAYS) + File.separator + nacosConfig.getSalt();
        systemSalt += File.separator + token;
        log.info("网关盐值信息:" + systemSalt);

        //网关签名处理
        gatewayResultSign(requestInfoModel, systemSalt, request);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 获取请求对象相关数据
     *
     * @param request 请求源
     * @author captain
     * @datetime 2021-09-27 14:03:33
     */
    public HttpModelDto getRequestInfoModel(ServerHttpRequest request) {
        //请求header参数
        var headers = request.getHeaders();
        Map<String, Object> headerMap = new TreeMap<>();
        for (String key : headers.keySet()) {
            headerMap.put(key.toLowerCase(), headers.getFirst(key));
        }

        //请求方式
        var methodValue = request.getMethodValue();

        //请求端口
        var port = String.valueOf(request.getURI().getPort());
        if (NumberEnum.MINUS_ONE.getCode().equals(port)) {
            port = ConstantEnum.DEFAULT_PORT.getCode();
        }

        //请求url
        var urlStr = request.getURI().toString();
        var ipAddress = RequestUtil.getIpAddress(request);
        if (urlStr.contains(ConstantEnum.LOCALHOST.getCode())) {
            urlStr = urlStr.replace(ConstantEnum.LOCALHOST.getCode(), ipAddress);
        }
        if (urlStr.contains(ConstantEnum.DEFAULT_IP.getCode())) {
            urlStr = urlStr.replace(ConstantEnum.DEFAULT_IP.getCode(), ipAddress);
        }

        //请求的URL上的参数
        var queryParamsMap = request.getQueryParams();
        Map<String, Object> urlParamMap = new TreeMap<>();
        for (String key : queryParamsMap.keySet()) {
            urlParamMap.put(key, queryParamsMap.get(key));
        }

        //请求的body参数
        Map<String, Object> bodyMap = new TreeMap<>();
        if (ConstantEnum.POST.getCode().equalsIgnoreCase(methodValue)) {
            var sb = new StringBuilder();
            var body = request.getBody();
            body.subscribe(buffer -> {
                var bytes = new byte[buffer.readableByteCount()];
                buffer.read(bytes);
                DataBufferUtils.release(buffer);
                var bodyString = new String(bytes, StandardCharsets.UTF_8);
                sb.append(bodyString);
            });
            if (StringUtil.isNotBlank(sb)) {
                bodyMap = JSONObject.parseObject(sb.toString(), new TypeReference<>() {
                });
            }

        }

        var httpModelDto = new HttpModelDto();
        httpModelDto.setHeaderMap(headerMap);
        httpModelDto.setMethodValue(methodValue);
        httpModelDto.setUrl(urlStr);
        httpModelDto.setUrlParamMap(urlParamMap);
        httpModelDto.setBodyParamMap(bodyMap);
        httpModelDto.setIpAddress(ipAddress);
        httpModelDto.setPort(port);
        httpModelDto.setUserAgent(String.valueOf(headers.get(ConstantEnum.USER_AGENT.getCode())));
        return httpModelDto;
    }

    /**
     * 定义绿色通道,不需要验证直接放行的请求
     *
     * @param obj 地址
     * @author captain
     * @datetime 2021-09-27 13:59:42
     */
    public boolean roadblock(Object obj) {
        //开发环境和本地环境关闭所有验证
        if ("dev".equalsIgnoreCase(nacosConfig.getAppActive()) || "local".equalsIgnoreCase(nacosConfig.getAppActive())) {
            return true;
        }

        if (obj instanceof String) {
            var url = String.valueOf(obj);
            //登录相关全部放行
            if (url.contains("/login")) return true;
            //swagger相关全部放行
            if (url.contains("/swagger") || url.contains("/api-docs")) return true;
            //druid相关全部放行
            if (url.contains("/druid")) return true;
        }
        return false;
    }

    /**
     * 签名并且缓存
     *
     * @param jsonDataSrc 缓存的数据源
     * @param key         缓存额key
     * @param num         数字
     * @param timeUnit    单位
     * @author captain
     * @datetime 2021-09-27 14:00:45
     */
    public String signAndCache(String jsonDataSrc, String key, int num, TimeUnit timeUnit) {
        var asymmetricModel = new AsymmetricModel();
        //获取密匙对
        var keyPairStr = redisDao.get(key);
        if (keyPairStr == null) {
            //初始化密钥对
            AsymmetricUtil.initKeyPair(asymmetricModel);
            //开启redis缓存密匙对
            var keyPairJson = JSONObject.toJSONString(asymmetricModel.getMyKeyPair());
            //设置密匙对时长30天,即每个月更换一次密匙对
            redisDao.set(key, keyPairJson, num, timeUnit);
        } else {
            var myKeyPair = JSON.parseObject(keyPairStr, MyKeyPair.class);
            asymmetricModel.setMyKeyPair(myKeyPair);
        }

        //设置签名数据
        asymmetricModel.setDataSource(jsonDataSrc);

        //网关私钥签名
        AsymmetricUtil.sign(asymmetricModel);

        return asymmetricModel.getSign();
    }

    public void gatewayResultSign(HttpModelDto httpModelDto, String systemSalt, ServerHttpRequest request) {
        var dataInfoJson = JSONObject.toJSONString(httpModelDto);
        //开始执行签名并且缓存
        var sign = signAndCache(dataInfoJson + systemSalt, nacosConfig.getAppName() + "_" + systemSalt + "_KeyPair", 7, TimeUnit.DAYS);
        log.info("网关签名信息:" + sign);

        //往请求头中添加网关签名
        Map<String, String> headersMap = new TreeMap<>();
        headersMap.put("gateway_sign", sign);
        headersMap.put("gateway_sign_data", dataInfoJson);

        request.mutate().headers(httpHeaders -> httpHeaders.setAll(headersMap)).build();
    }
}
