package com.wisdom.gateway.adapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.wisdom.config.dto.HttpModelDto;
import com.wisdom.config.enums.ResultEnum;
import com.wisdom.gateway.tools.request.RequestUtil;
import com.wisdom.gateway.tools.response.ResponseUtil;
import com.wisdom.tools.algorithm.asymmetric.AsymmetricModel;
import com.wisdom.tools.algorithm.asymmetric.AsymmetricUtil;
import com.wisdom.tools.algorithm.asymmetric.MyKeyPair;
import com.wisdom.tools.string.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
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
    @Value("${spring.application.name:gateway}")
    private String appName;

    @Value("${common.params.token_key:token}")
    private String tokenKey;

    @Value("${spring.profiles.active:dev}")
    private String appActive;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //通用请求数据对象
        HttpModelDto requestInfoModel = getRequestInfoModel(request);
        log.info("》通用请求记录开始【{}】通用请求记录结束《", JSONObject.toJSONString(requestInfoModel));

        boolean roadblock = roadblock(requestInfoModel.getUrl());
        if (roadblock) { //不用检查,放行
            return chain.filter(exchange);
        }
        //验证token  后期优化
        String baseToken = request.getHeaders().getFirst(tokenKey);
        if (StringUtil.isBlank(baseToken)) {
            return ResponseUtil.resultMsgToMono(ResultEnum.TOKEN_IS_EMPTY, response);
        }

        //签名数据
        HttpModelDto checkInfoModel = getCheckInfoModel(requestInfoModel);

        String signData = JSONObject.toJSONString(checkInfoModel);

        //开始执行签名并且缓存
        String sign = signAndCache(signData, appName + "_" + requestInfoModel.getSalt() + "_KeyPair", 7, TimeUnit.DAYS);

        //往请求头中添加网关签名
        Map<String, String> headersMap = new TreeMap<>();
        headersMap.put("gateway_name", appName);
        headersMap.put(appName + "_sign", sign);
        headersMap.put(appName + "_url", requestInfoModel.getUrl());
        request.mutate().headers(httpHeaders -> httpHeaders.setAll(headersMap)).build();

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
        HttpHeaders headers = request.getHeaders();
        Map<String, Object> headerMap = new TreeMap<>();
        for (String key : headers.keySet()) {
            headerMap.put(key, headers.get(key));
        }

        //请求方式
        String methodValue = request.getMethodValue();

        //请求url
        URI uri = request.getURI();
        String urlStr = uri.toString();
        String ipAddress = RequestUtil.getIpAddress(request);
        if (urlStr.contains("localhost")) {
            urlStr = urlStr.replace("localhost", ipAddress);
        }
        if (urlStr.contains("127.0.0.1")) {
            urlStr = urlStr.replace("127.0.0.1", ipAddress);
        }

        //请求的URL上的参数
        MultiValueMap<String, String> queryParamsMap = request.getQueryParams();
        Map<String, Object> urlParamMap = new TreeMap<>();
        for (String key : queryParamsMap.keySet()) {
            urlParamMap.put(key, queryParamsMap.get(key));
        }

        HttpModelDto httpModelDto = new HttpModelDto();
        httpModelDto.setHeaderMap(headerMap);
        httpModelDto.setMethodValue(methodValue);
        httpModelDto.setUrl(urlStr);
        httpModelDto.setUrlParamMap(urlParamMap);

        //请求的body参数
        if ("POST".equalsIgnoreCase(methodValue)) {
            StringBuilder sb = new StringBuilder();
            Flux<DataBuffer> body = request.getBody();
            body.subscribe(buffer -> {
                byte[] bytes = new byte[buffer.readableByteCount()];
                buffer.read(bytes);
                DataBufferUtils.release(buffer);
                String bodyString = new String(bytes, StandardCharsets.UTF_8);
                sb.append(bodyString);
            });
            if (StringUtil.isNotBlank(sb)) {
                TreeMap<String, Object> bodyMap = JSONObject.parseObject(sb.toString(), new TypeReference<>() {
                });
                httpModelDto.setBodyParamMap(bodyMap);
            }

        }

        return httpModelDto;
    }

    /**
     * 定义允许通过的请求
     *
     * @param obj 地址
     * @author captain
     * @datetime 2021-09-27 13:59:42
     */
    public boolean roadblock(Object obj) {
        //开发环境和本地环境关闭所有验证
        if ("dev".equalsIgnoreCase(appActive) || "local".equalsIgnoreCase(appActive)) {
//            return true;
        }

        if (obj instanceof String) {
            String url = String.valueOf(obj);
            //登录相关全部放行
            if (url.contains("/login.html") || url.contains("/index.html")) {
                return true;
            }
            //swagger相关全部放行
            if (url.contains("/swagger") || url.contains("/api-docs")) {
                return true;
            }
            //druid相关全部放行
            if (url.contains("/druid")) {
                return true;
            }
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
        AsymmetricModel asymmetricModel = new AsymmetricModel();
        //获取密匙对
        String keyPairStr = stringRedisTemplate.opsForValue().get(key);
        if (keyPairStr == null) {
            //初始化密钥对
            AsymmetricUtil.initKeyPair(asymmetricModel);
            //开启redis缓存密匙对
            String keyPairJson = JSONObject.toJSONString(asymmetricModel.getMyKeyPair());
            //设置密匙对时长30天,即每个月更换一次密匙对
            stringRedisTemplate.opsForValue().set(key, keyPairJson, num, timeUnit);
        } else {
            MyKeyPair myKeyPair = JSON.parseObject(keyPairStr, MyKeyPair.class);
            asymmetricModel.setMyKeyPair(myKeyPair);
        }

        //设置签名数据
        asymmetricModel.setDataSource(jsonDataSrc);

        //网关私钥签名
        AsymmetricUtil.sign(asymmetricModel);

        return asymmetricModel.getSign();
    }

    /**
     * 创建用于签名对象
     *
     * @param httpModelDto 数据源
     * @author admin
     * @datetime 2021-09-27 14:02:44
     */
    public HttpModelDto getCheckInfoModel(HttpModelDto httpModelDto) {
        HttpModelDto httpCheckModelDto = new HttpModelDto();
        httpCheckModelDto.setUrl(httpModelDto.getUrl());
        httpCheckModelDto.setMethodValue(httpModelDto.getMethodValue());
        httpCheckModelDto.setUrlParamMap(httpModelDto.getUrlParamMap());
        httpCheckModelDto.setBodyParamMap(httpModelDto.getBodyParamMap());
        return httpCheckModelDto;
    }
}
