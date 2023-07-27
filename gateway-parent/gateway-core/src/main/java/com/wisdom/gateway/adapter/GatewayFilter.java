package com.wisdom.gateway.adapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.wisdom.base.dto.HttpModelDto;
import com.wisdom.base.enums.ConstantEnum;
import com.wisdom.base.enums.DateTimeEnum;
import com.wisdom.base.enums.NumberEnum;
import com.wisdom.base.enums.ResultEnum;
import com.wisdom.base.redis.RedisService;
import com.wisdom.gateway.config.NacosConfig;
import com.wisdom.gateway.tools.request.RequestUtil;
import com.wisdom.gateway.tools.response.ResponseUtil;
import com.wisdom.tools.certificate.asymmetric.AsymmetricModel;
import com.wisdom.tools.certificate.asymmetric.AsymmetricUtil;
import com.wisdom.tools.certificate.asymmetric.MyKeyPair;
import com.wisdom.tools.datetime.DateUtilByZoned;
import com.wisdom.tools.string.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    private RedisService redisService;

    @Autowired
    private NacosConfig nacosConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //通用请求数据对象
        HttpModelDto httpModelDto = getRequestInfoModel(request);
        String requestInfoModelJson = JSON.toJSONString(httpModelDto);
        log.info("》通用请求记录开始【{}】通用请求记录结束《", requestInfoModelJson);

        //关隘:判断不需要验证的,放行
        boolean roadblock = roadblock(httpModelDto);
        if (roadblock) {
            return chain.filter(exchange);
        }

        if (httpModelDto.getHeaderMap().size() == 0) {
            return ResponseUtil.resultMsgToMono(ResultEnum.RESULT_ENUM_1012, response);
        }

        //验证token
        String token = (String) httpModelDto.getHeaderMap().get(nacosConfig.getTokenKey());
        if (StringUtil.isBlank(token)) {
            return ResponseUtil.resultMsgToMono(ResultEnum.RESULT_ENUM_1008, response);
        }

        String dateTime = DateUtilByZoned.getDateTime(DateTimeEnum.DATETIME_PATTERN_MILLI_UN);
        MDC.put("BIZ_ID", dateTime);

        String dateTimeSalt = redisService.save(nacosConfig.getSaltKey(), dateTime, NumberEnum.SEVEN.getCodeToInt(), TimeUnit.DAYS);
        //设置网关盐值信息
        String systemSalt = nacosConfig.getSalt() + token + dateTimeSalt;

        //网关签名处理
        gatewayResultSign(httpModelDto, systemSalt, dateTime, request);
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
            headerMap.put(key.toLowerCase(), headers.getFirst(key));
        }

        //请求方式
        String methodValue = request.getMethodValue();

        //请求端口
        String port = String.valueOf(request.getURI().getPort());
        if (NumberEnum.MINUS_ONE.getCode().equals(port)) {
            port = ConstantEnum.DEFAULT_PORT.getCode();
        }

        //请求url
        String urlStr = request.getURI().toString();
        var ipAddress = RequestUtil.getIpAddress(request);
        if (urlStr.contains(ConstantEnum.LOCALHOST.getCode())) {
            urlStr = urlStr.replace(ConstantEnum.LOCALHOST.getCode(), ipAddress);
        }
        if (urlStr.contains(ConstantEnum.DEFAULT_IP.getCode())) {
            urlStr = urlStr.replace(ConstantEnum.DEFAULT_IP.getCode(), ipAddress);
        }

        //请求的URL上的参数
        MultiValueMap<String, String> queryParamsMap = request.getQueryParams();
        Map<String, Object> urlParamMap = new TreeMap<>();
        for (String key : queryParamsMap.keySet()) {
            urlParamMap.put(key, queryParamsMap.get(key));
        }

        HttpModelDto httpModelDto = new HttpModelDto();

        //请求的body参数
        MediaType mediaType = request.getHeaders().getContentType();
        if (RequestUtil.isUploadFile(mediaType)) {
            httpModelDto.setBodyParamMap("文件上传!");
        } else {
            Map<String, Object> bodyMap;
            if (ConstantEnum.POST.getCode().equalsIgnoreCase(methodValue)) {
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
                    bodyMap = JSON.parseObject(sb.toString(), new TypeReference<>() {
                    });
                    httpModelDto.setBodyParamMap(bodyMap);
                }
            }
        }

        httpModelDto.setHeaderMap(headerMap);
        httpModelDto.setMethodValue(methodValue);
        httpModelDto.setUrl(urlStr);
        httpModelDto.setUrlParamMap(urlParamMap);
        httpModelDto.setIpAddress(ipAddress);
        httpModelDto.setPort(port);
        httpModelDto.setUserAgent(String.valueOf(headers.get(ConstantEnum.USER_AGENT.getCode())));
        return httpModelDto;
    }

    /**
     * 定义绿色通道,不需要验证直接放行的请求
     *
     * @param httpModelDto 请求对象
     * @author captain
     * @datetime 2021-09-27 13:59:42
     */
    public boolean roadblock(HttpModelDto httpModelDto) {
        //开发环境和本地环境关闭所有验证
        //if ("dev".equalsIgnoreCase(nacosConfig.getAppActive()) || "local".equalsIgnoreCase(nacosConfig.getAppActive())) return true;

        String url = String.valueOf(httpModelDto.getUrl());
        //登录相关全部放行
        if (url.contains("/login")) return true;
        //swagger相关全部放行
        if (url.contains("/swagger") || url.contains("/api-docs")) return true;
        //druid相关全部放行
        if (url.contains("/druid")) return true;
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
        var keyPairStr = redisService.get(key);
        if (keyPairStr == null) {
            //初始化密钥对
            AsymmetricUtil.initKeyPair(asymmetricModel);
            //开启redis缓存密匙对
            var keyPairJson = JSON.toJSONString(asymmetricModel.getMyKeyPair());
            //设置密匙对时长30天,即每个月更换一次密匙对
            redisService.set(key, keyPairJson, num, timeUnit);
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

    public void gatewayResultSign(HttpModelDto httpModelDto, String systemSalt, String bizId, ServerHttpRequest request) {
        String bodyMd5 = DigestUtils.md5Hex(JSON.toJSONString(httpModelDto));

        String systemSaltMD5 = DigestUtils.md5Hex(nacosConfig.getAppName() + "_" + systemSalt + "_KeyPair");

        //开始执行签名并且缓存
        var sign = signAndCache(bodyMd5 + systemSaltMD5, systemSaltMD5, NumberEnum.SEVEN.getCodeToInt(), TimeUnit.DAYS);
        log.info("网关签名信息:" + sign);

        //往请求头中添加网关签名
        Map<String, String> headersMap = new TreeMap<>();
        headersMap.put("gatewaysign", sign);
        headersMap.put("gatewaysignmd5", bodyMd5);
        headersMap.put("bizid", bizId);

        request.mutate().headers(httpHeaders -> httpHeaders.setAll(headersMap)).build();
    }


}
