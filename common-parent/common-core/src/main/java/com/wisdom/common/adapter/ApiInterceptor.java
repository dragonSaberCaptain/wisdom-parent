package com.wisdom.common.adapter;

import com.wisdom.common.tools.request.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

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
    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, String[]> rec = request.getParameterMap();
        Map<String, Object> result = new LinkedHashMap<>();

        for (Map.Entry<String, String[]> entry : rec.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue()[0];
            result.put(name, value);
            log.info("请求参数:" + name + ", value:" + value);
        }
        return result;
    }

    /**
     * controller 执行之前调用
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        log.info("请求地址： " + url);
        String ip = RequestUtil.getIpAddress(request);
        log.info("请求ip： " + ip);
        String agent = request.getHeader("User-Agent");
        log.info("请求来源： " + agent);

        //        判断请求是否来自手机 返回true表示是手机
//        boolean judgeismoblie = VerifyUtil.judgeIsMoblie(httpServletRequest);
//        log.info("是否来自手机:" + judgeismoblie);
//        if (Global.OPEN_DEV && !judgeismoblie) {
//            return true;
//        }
        Map<String, Object> rec = new LinkedHashMap<>();

        Map<String, Object> requestMap = getParams(request);

        String token = (String) requestMap.get("token");
        String signMsg = (String) requestMap.get("signMsg");
        String uuid = (String) requestMap.get("uuid");

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
}
