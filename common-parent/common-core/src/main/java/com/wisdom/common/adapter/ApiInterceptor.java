package com.wisdom.common.adapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.wisdom.common.tools.request.RequestUtil;
import com.wisdom.common.tools.response.ResponseUtil;
import com.wisdom.config.enums.HttpEnum;
import com.wisdom.config.enums.ResultEnum;
import com.wisdom.config.exception.ResultException;
import com.wisdom.tools.algorithm.asymmetric.AsymmetricModel;
import com.wisdom.tools.algorithm.asymmetric.AsymmetricUtil;
import com.wisdom.tools.algorithm.asymmetric.MyKeyPair;
import com.wisdom.tools.string.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
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
    @Value("${spring.profiles.active:dev}")
    private String appActive;

    @Value("${common.params.token_key:token}")
    private String tokenKey;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final String METHOD_VALUE = "methodValue";
    private final String URL = "url";
    private final String PORT = "port";
    private final String URL_PARAM_MAP = "urlParamMap";
    private final String BODY_PARAM_MAP = "bodyParamMap";
    private final String SALT = "pq$69.salt";


    /**
     * controller 执行之前调用
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ipAddress = RequestUtil.getIpAddress(request);
        log.info("请求ip【{}】", ipAddress);
        log.info("请求url【{}】", request.getRequestURL());
        String userAgent = request.getHeader("User-Agent");
        log.info("请求来源【{}】", userAgent);

        //开发环境和本地环境关闭所有验证
        if ("dev".equalsIgnoreCase(appActive) || "local".equalsIgnoreCase(appActive)) {
//            return true;
        }

        //来自网关的服务器名称
        String gatewayName = request.getHeader("gateway_name");
        //来自网关的客户端url
        String gatewayUrl = request.getHeader(gatewayName + "_url");
        //来自网关的签名
        String gatewaySign = request.getHeader(gatewayName + "_sign");

        //获取网关信息,只处理从网关过来的请求
        if (StringUtil.isBlank(gatewayName) || StringUtil.isBlank(gatewayUrl) || StringUtil.isBlank(gatewaySign)) {
            log.error("数据来源异常,gatewayName={},gatewayUrl={},gatewaySign={}", gatewayName, gatewayUrl, gatewaySign);
            throw new ResultException(HttpEnum.BAD_GATEWAY);
        }

        String methodValue = request.getMethod();

        Map<String, String[]> urlParamMap = request.getParameterMap();

        //处理url参数数据,升序排序
        Map<String, String[]> sortUrlParamMap = new TreeMap<>(urlParamMap);

        TreeMap<String, Object> bodyMap = null;
        if ("POST".equalsIgnoreCase(methodValue)) {
            String bodyString = RepeatedlyReadRequestWrapper.getBodyToString(request);
            bodyMap = JSONObject.parseObject(bodyString, new TypeReference<>() {
            });
        }

        Map<String, Object> signDataMap = new TreeMap<>();
        signDataMap.put(URL, gatewayUrl);
        signDataMap.put(METHOD_VALUE, methodValue);
        signDataMap.put(URL_PARAM_MAP, sortUrlParamMap);
        signDataMap.put(BODY_PARAM_MAP, bodyMap);

        String signData = JSONObject.toJSONString(signDataMap);
        log.info("》》》》签名验证开始【{}】签名验证结束《《《《", signData);

        //验证签名是否通过
        boolean check = checkSignByCache(signData, gatewaySign, gatewayName + "_" + SALT + "_KeyPair");
        if (!check) {
            throw new ResultException(HttpEnum.BAD_GATEWAY);
        }

//        判断请求是否来自手机 返回true表示是手机
//        boolean judgeismoblie = VerifyUtil.judgeIsMoblie(httpServletRequest);
//        log.info("是否来自手机:" + judgeismoblie);
//        if (Global.OPEN_DEV && !judgeismoblie) {
//            return true;
//        }
        Map<String, Object> rec = new LinkedHashMap<>();


//        String token = (String) requestMap.get(tokenKey);

//        if (StringUtil.isBlank(token)) {
//            rec.put(Global.CODE, UserEnum.TOKEN_NOT_EMPTY.getCode());
//            rec.put(Global.MSG, UserEnum.TOKEN_NOT_EMPTY.getMsg());
//            ResultUtil.writeJson(rec, httpServletResponse);
//            return false;
//        }
        //从缓存中获取token
//        String webToken;
//        try {
//            webToken = JedisUtil.Strings.get(Global.LOGIN_VALID_TOKEN + token);
//        } catch (Exception e) {
//            log.error("ApiInterceptor--preHandle", e);
//            e.printStackTrace();
//            rec.put(Global.CODE, UserEnum.TOKEN_NO_FIND.getCode());
//            rec.put(Global.MSG, UserEnum.TOKEN_NO_FIND.getMsg());
//            ResultUtil.writeJson(rec, httpServletResponse);
//            return false;
//        }
        //验证前端传入的token与后台存储的token是否一致
//        if (StringUtil.isNotBlank(webToken)) {
//            //验证流程
//            String webSignMsg = DigestUtils.md5Hex(Global.MD5_SALT + uuid).toUpperCase();
//            boolean bool = webSignMsg.equalsIgnoreCase(signMsg);
//            if (bool) {
//                log.info("ApiInterceptor--preHandle—ok", "验证通过");
//                return true;
//            }
//        }
//
//        rec.put(Global.CODE, UserEnum.TOKEN_ERROR.getCode());
//        rec.put(Global.MSG, UserEnum.TOKEN_ERROR.getMsg());
//        ResultUtil.writeJson(rec, httpServletResponse);
        return true;
    }

    /**
     * controller 执行之后，且页面渲染之前调用
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("------postHandle-----");
    }

    /**
     * 页面渲染之后调用，一般用于资源清理操作
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("------afterCompletion-----");
    }

//    private Map<String, Object> getParams(HttpServletRequest request) {
//        Map<String, String[]> rec = request.getParameterMap();
//        Map<String, Object> result = new TreeMap<>();
//
//        for (Map.Entry<String, String[]> entry : rec.entrySet()) {
//            String name = entry.getKey();
//            Object value = entry.getValue()[0];
//            result.put(name, value);
//        }
//        return result;
//    }

    public boolean checkSignByCache(String jsonDataSrc, String signSrc, String key) {
        //获取密匙对
        String keyPairStr = stringRedisTemplate.opsForValue().get(key);
        if (keyPairStr == null) {
            return false;
        }
        MyKeyPair myKeyPair = JSON.parseObject(keyPairStr, MyKeyPair.class);

        AsymmetricModel asymmetricModel = new AsymmetricModel();
        asymmetricModel.setMyKeyPair(myKeyPair);
        asymmetricModel.setDataSource(jsonDataSrc);
        asymmetricModel.setSign(signSrc);
        AsymmetricUtil.signVerify(asymmetricModel);

        return asymmetricModel.isSignVerifyResult();
    }
}
