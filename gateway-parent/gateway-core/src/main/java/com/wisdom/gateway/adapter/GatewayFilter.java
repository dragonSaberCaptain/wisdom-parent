package com.wisdom.gateway.adapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
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

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final String HEADER_MAP = "headerMap";
    private final String METHOD_VALUE = "methodValue";
    private final String URL = "url";
    private final String URL_PARAM_MAP = "urlParamMap";
    private final String BODY_PARAM_MAP = "bodyParamMap";
    private final String SALT = "pq$69.salt";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //获取请求信息数据
        Map<String, Object> requestInfoMap = getRequestInfo(request);

        String infoStr = JSONObject.toJSONString(requestInfoMap);
        log.info("》》》》请求信息开始【{}】请求信息结束《《《《", infoStr);

        Object url = requestInfoMap.get(URL);
        Object methodValue = requestInfoMap.get(METHOD_VALUE);
        Object urlParamMap = requestInfoMap.get(URL_PARAM_MAP);
        Object bodyParamMap = requestInfoMap.get(BODY_PARAM_MAP);

        boolean roadblock = roadblock(url);
        if (!roadblock) { //不用检查,放行
            return chain.filter(exchange);
        }
        //验证token
        String baseToken = request.getHeaders().getFirst(tokenKey);
        if (StringUtil.isBlank(baseToken)) {
            return ResponseUtil.resultMsgToMono(ResultEnum.TOKEN_IS_EMPTY, response);
        }

        //签名数据
        Map<String, Object> signMap = new TreeMap<>();
        signMap.put(URL, url);
        signMap.put(METHOD_VALUE, methodValue);
        signMap.put(URL_PARAM_MAP, urlParamMap);
        signMap.put(BODY_PARAM_MAP, bodyParamMap);
        String signMapStr = JSONObject.toJSONString(signMap);
        log.info("》》》》签名信息开始【{}】签名信息结束《《《《", signMapStr);

        //开始执行签名并且缓存
        String sign = signAndCache(signMapStr, appName + "_" + SALT + "_KeyPair", 30, TimeUnit.DAYS);

        //往请求头中添加网关签名
        Map<String, String> headersMap = new TreeMap<>();
        headersMap.put("gateway_name", appName);
        headersMap.put(appName + "_sign", sign);
        headersMap.put(appName + "_url", String.valueOf(url));
        request.mutate().headers(httpHeaders -> httpHeaders.setAll(headersMap)).build();

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public Map<String, Object> getRequestInfo(ServerHttpRequest request) {
        Map<String, Object> infoMap = new TreeMap<>();

        //请求header参数
        HttpHeaders headers = request.getHeaders();
        Map<String, Object> headerMap = new TreeMap<>();
        for (String key : headers.keySet()) {
            headerMap.put(key, headers.get(key));
        }
        infoMap.put(HEADER_MAP, headerMap);

        //请求方式
        String methodValue = request.getMethodValue();
        infoMap.put(METHOD_VALUE, methodValue);

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
        infoMap.put(URL, urlStr);

        //请求的URL上的参数
        MultiValueMap<String, String> queryParamsMap = request.getQueryParams();
        Map<String, Object> urlParamMap = new TreeMap<>();
        for (String key : queryParamsMap.keySet()) {
            urlParamMap.put(key, queryParamsMap.get(key));
        }
        infoMap.put(URL_PARAM_MAP, urlParamMap);

        //请求的body参数
        StringBuilder sb = new StringBuilder();
        TreeMap<String, Object> bodyMap = null;
        if ("POST".equalsIgnoreCase(methodValue)) {
            Flux<DataBuffer> body = request.getBody();
            body.subscribe(buffer -> {
                byte[] bytes = new byte[buffer.readableByteCount()];
                buffer.read(bytes);
                DataBufferUtils.release(buffer);
                String bodyString = new String(bytes, StandardCharsets.UTF_8);
                sb.append(bodyString);
            });
            bodyMap = JSONObject.parseObject(sb.toString(), new TypeReference<>() {
            });
        }
        infoMap.put(BODY_PARAM_MAP, bodyMap);

        return infoMap;
    }

    public boolean roadblock(Object obj) {
        if (obj instanceof String) {
            String url = String.valueOf(obj);
            //登录相关全部放行
            if (url.contains("/login.html") || url.contains("/index.html")) {
                return false;
            }
            //swagger相关全部放行
            if (url.contains("/swagger") || url.contains("/api-docs")) {
                return false;
            }
            //druid相关全部放行
            if (url.contains("/druid")) {
                return false;
            }
        }
        return true;
    }

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
}
