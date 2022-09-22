package com.wisdom.common.adapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.wisdom.common.tools.request.RequestUtil;
import com.wisdom.config.dto.HttpModelDto;
import com.wisdom.config.enums.ConstantEnum;
import com.wisdom.config.enums.HttpEnum;
import com.wisdom.config.exception.ResultException;
import com.wisdom.config.params.NacosCommonConfig;
import com.wisdom.tools.certificate.asymmetric.AsymmetricModel;
import com.wisdom.tools.certificate.asymmetric.AsymmetricUtil;
import com.wisdom.tools.certificate.asymmetric.MyKeyPair;
import com.wisdom.tools.database.RedisDao;
import com.wisdom.tools.string.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * TODO (用一句话描述该类的作用)
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/15 18:10 星期三
 */
@Slf4j
public class ApiInterceptor implements HandlerInterceptor {
    @Autowired
    private NacosCommonConfig nacosCommonConfig;

    @Autowired
    private RedisDao redisDao;

    /**
     * controller 执行之前调用
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        var bizId = request.getHeader("bizid");
        MDC.put("BIZ_ID", bizId);

        //通用请求数据对象
        var requestInfoModel = getRequestInfoModel(request);
        var requestInfoModelJson = JSONObject.toJSONString(requestInfoModel);
        log.info("》通用请求记录开始【{}】通用请求记录结束《", requestInfoModelJson);

        //关隘:判断不需要验证的,放行
        var roadblock = roadblock(requestInfoModel.getUrl());
        if (roadblock) {
            return true;
        }

        //来自网关的签名
        var gatewaySign = request.getHeader("gatewaysign");
        var gatewaySignMd5 = request.getHeader("gatewaysignmd5");

        //获取网关信息,只处理从网关过来的请求
        if (StringUtil.isBlank(gatewaySign) || StringUtil.isBlank(gatewaySignMd5)) {
            throw new ResultException(HttpEnum.BAD_GATEWAY);
        }

        //获取token
        var token = request.getHeaders(nacosCommonConfig.getTokenKey()).nextElement();

        var dateTimeSalt = redisDao.get(nacosCommonConfig.getSaltKey());
        var systemSalt = nacosCommonConfig.getSalt() + token + dateTimeSalt;

        //验证签名是否通过
        String key = DigestUtils.md5Hex("gateway" + "_" + systemSalt + "_KeyPair");
        var check = checkGatewaySign(gatewaySignMd5 + key, gatewaySign, key);
        if (!check) {
            log.info("签名验证不通过");
            throw new ResultException(HttpEnum.BAD_GATEWAY);
        }
        return true;
    }

    /**
     * controller 执行之后，且页面渲染之前调用
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        log.info("------postHandle-----");
    }

    /**
     * 页面渲染之后调用，一般用于资源清理操作
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.info("------afterCompletion-----");
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
        if ("dev".equalsIgnoreCase(nacosCommonConfig.getAppActive()) || "local".equalsIgnoreCase(nacosCommonConfig.getAppActive())) {
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
     * 从缓存中获取key,用于验证签名是否正确
     *
     * @param jsonDataSrc 数据源
     * @param sign        签名字符串
     * @param key         key值
     * @author captain
     * @datetime 2021-09-27 14:06:22
     */
    public boolean checkGatewaySign(String jsonDataSrc, String sign, String key) {
        //获取密匙对
        String keyPairStr = redisDao.get(key);
        if (keyPairStr == null) {
            return false;
        }
        MyKeyPair myKeyPair = JSON.parseObject(keyPairStr, MyKeyPair.class);

        AsymmetricModel asymmetricModel = new AsymmetricModel();
        asymmetricModel.setMyKeyPair(myKeyPair);
        asymmetricModel.setDataSource(jsonDataSrc);
        asymmetricModel.setSign(sign);
        AsymmetricUtil.signVerify(asymmetricModel);

        return asymmetricModel.isSignVerifyResult();
    }

    /**
     * 获取请求相关数据
     *
     * @param request 请求源
     * @author captain
     * @datetime 2021-09-27 14:08:03
     */
    public HttpModelDto getRequestInfoModel(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, Object> headerMap = new TreeMap<>();

        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            headerMap.put(key, value);
        }

        String methodValue = request.getMethod();
        Map<String, String[]> urlParamMap = request.getParameterMap();

        String ipAddress = RequestUtil.getIpAddress(request);

        Map<String, Object> bodyMap = new TreeMap<>();
        if ("POST".equalsIgnoreCase(methodValue)) {
            String bodyString = RepeatedlyReadRequestWrapper.getBodyToString(request);
            if (StringUtil.isNotBlank(bodyString)) {
                bodyMap = JSONObject.parseObject(bodyString, new TypeReference<>() {
                });
            }
        }

        HttpModelDto httpModelDto = new HttpModelDto();
        httpModelDto.setIpAddress(ipAddress);
        httpModelDto.setUrl(request.getRequestURL().toString());
        httpModelDto.setPort(ConstantEnum.DEFAULT_PORT.getCode());
        httpModelDto.setMethodValue(methodValue);
        httpModelDto.setUrlParamMap(new TreeMap<>(urlParamMap));
        httpModelDto.setBodyParamMap(bodyMap);
        httpModelDto.setHeaderMap(headerMap);

        return httpModelDto;
    }
}
