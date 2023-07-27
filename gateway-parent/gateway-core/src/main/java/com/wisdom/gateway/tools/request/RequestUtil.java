package com.wisdom.gateway.tools.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * 获取ip地址
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/16 11:14 星期四
 */
@Slf4j
public class RequestUtil {
    public static String getIpAddress(final HttpServletRequest request) {
        var ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress != null && ipAddress.length() != 0 && !"unknown".equalsIgnoreCase(ipAddress)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ipAddress.contains(",")) {
                ipAddress = ipAddress.split(",")[0];
            }
        }

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if ("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress) || "localhost".equals(ipAddress)) {
                ipAddress = getLocalIpFromWin();
            }
        }
        return ipAddress;
    }

    public static String getIpAddress(final ServerHttpRequest request) {
        var headers = request.getHeaders();
        var ipAddress = headers.getFirst("x-forwarded-for");
        if (ipAddress != null && ipAddress.length() != 0 && !"unknown".equalsIgnoreCase(ipAddress)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ipAddress.contains(",")) {
                ipAddress = ipAddress.split(",")[0];
            }
        }

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = headers.getFirst("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = headers.getFirst("X-Real-IP");
        }

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();
            if ("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress) || "localhost".equals(ipAddress)) {
                ipAddress = getLocalIpFromWin();
            }
        }
        return ipAddress;
    }

    /**
     * 获取Windows服务器的本地IP
     */
    private static String getLocalIpFromWin() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 判断是否是上传文件
     *
     * @param mediaType MediaType
     * @return Boolean
     */
    public static boolean isUploadFile(@Nullable MediaType mediaType) {
        //String contentType = ctx.getContentType();
        //return contentType == null ? false : contentType.toLowerCase(Locale.ENGLISH).startsWith("multipart/");

        if (Objects.isNull(mediaType)) {
            return false;
        }
        return MediaType.MULTIPART_FORM_DATA.equals(mediaType)
                || MediaType.IMAGE_GIF.equals(mediaType)
                || MediaType.IMAGE_JPEG.equals(mediaType)
                || MediaType.IMAGE_PNG.equals(mediaType)
                || "multipart".equals(mediaType.getType());
    }
}
