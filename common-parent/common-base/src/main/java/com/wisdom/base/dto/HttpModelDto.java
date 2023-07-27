package com.wisdom.base.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * 用于网关与服务间通讯时数据的存储对象
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/27 11:07 星期一
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpModelDto {
    /**
     * 请求方法
     */
    private String methodValue;
    /**
     * 请求url
     */
    private String url;

    /**
     * 请求路径
     */
    private String servletPath;
    /**
     * 请求port
     */
    private String port;
    /**
     * 请求ip地址
     */
    private String ipAddress;
    /**
     * 请求来源
     */
    private String userAgent;

    private String bizId;
    /**
     * 请求url带的参数
     */
    private Map<String, Object> urlParamMap;
    /**
     * 请求body参数
     */
    private Object bodyParamMap;
    /**
     * 请求头参数
     */
    private Map<String, Object> headerMap;
}
