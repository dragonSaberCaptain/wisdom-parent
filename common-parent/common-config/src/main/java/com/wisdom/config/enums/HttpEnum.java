package com.wisdom.config.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.wisdom.config.annotation.ResultFiled;
import org.apache.commons.lang3.StringUtils;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @version 1.0
 * @description http相关状态码 请严格按照规范进行扩展
 * @date 2021/6/30 14:42 星期三
 */
public enum HttpEnum implements EnumDao {
    // ****************************【1XX 信息，服务器收到请求，需要请求者继续执行操作】****************************
    CONTINUE("100", "继续"),

    SWITCHING_PROTOCOLS("101", "切换协议"),

    // ****************************【2XX 成功，操作被成功接收并处理】*********************************************
    OK("200", "成功"),

    CREATED("201", "已创建"),

    ACCEPTED("202", "已接受"),

    NON_AUTHORITATIVE_INFORMATION("203", "非授权信息"),

    NO_CONTENT("204", "无内容"),

    RESET_CONTENT("205", "重置内容"),

    PARTIAL_CONTENT("206", "部分内容"),

    // ****************************【3XX 重定向，需要进一步的操作以完成请求】**************************************
    MULTIPLE_CHOICES("300", "多种选择"),

    MOVED_PERMANENTLY("301", "永久移动"),

    FOUND("302", "临时移动"),

    SEE_OTHER("303", "查看其他位置"),

    NOT_MODIFIED("304", "未修改"),

    USE_PROXY("305", "使用代理"),

    TEMPORARY_REDIRECT("307", "临时重定向"),

    // ****************************【4XX 客户端错误，请求包含语法错误或无法完成请求】********************************
    BAD_REQUEST("400", "错误请求"),

    UNAUTHORIZED("401", "未授权"),

    FORBIDDEN("403", "已禁止"),

    NOT_FOUND("404", "未找到"),

    METHOD_NOT_ALLOWED("405", "方法禁用"),

    NOT_ACCEPTABLE("406", "不接受"),

    PROXY_AUTHENTICATION_REQUIRED("407", "需要代理授权"),

    REQUEST_TIMEOUT("408", "请求超时"),

    CONFLICT("409", "冲突"),

    GONE("410", "已删除"),

    LENGTH_REQUIRED("411", "需要有效长度"),

    PRECONDITION_FAILED("412", "未满足前提条件"),

    REQUEST_ENTITY_TOO_LARGE("413", "请求实体过大"),

    REQUEST_URI_TOO_LONG("414", "请求的URI过长"),

    UNSUPPORTED_MEDIA_TYPE("415", "不支持的媒体类型"),

    REQUESTED_RANGE_NOT_SATISFIABLE("416", "请求范围不符合要求"),

    EXPECTATION_FAILED("417", "未满足期望值"),

    // ****************************【5XX 服务器错误，服务器在处理请求的过程中发生了错误】*******************************
    INTERNAL_SERVER_ERROR("500", "服务器内部错误"),

    NOT_IMPLEMENTED("501", "尚未实施"),

    BAD_GATEWAY("502", "错误网关"),

    SERVICE_UNAVAILABLE("503", "服务不可用"),

    GATEWAY_TIMEOUT("504", "网关超时"),

    HTTP_VERSION_NOT_SUPPORTED("505", "HTTP 版本不受支持");

    //    @JsonValue
    @EnumValue //mybatis_plus需要,若不需要可以删除
    @ResultFiled
    private String code;
    @ResultFiled
    private String msg;
    @ResultFiled
    private String subMsg;

    HttpEnum() {
    }

    HttpEnum(String code) {
        this.code = code;
    }

    HttpEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    HttpEnum(String code, String msg, String subMsg) {
        this.code = code;
        this.msg = msg;
        this.subMsg = subMsg;
    }

    public static HttpEnum findByCode(String code) {
        if (StringUtils.isNotBlank(code)) {
            for (HttpEnum resultEnum : values()) {
                if (code.equals(resultEnum.getCode())) {
                    return resultEnum;
                }
            }
        }
        return null;
    }

    public static HttpEnum findByCode(long code) {
        return findByCode(String.valueOf(code));
    }

    public static HttpEnum findByMsg(String msg) {
        if (StringUtils.isNotBlank(msg)) {
            for (HttpEnum resultEnum : values()) {
                if (msg.equals(resultEnum.getMsg())) {
                    return resultEnum;
                }
            }
        }
        return null;
    }

    public final int findCodeByMsg(String msg) {
        if (StringUtils.isNotBlank(msg)) {
            for (HttpEnum resultEnum : values()) {
                if (msg.equals(resultEnum.getMsg())) {
                    return Integer.parseInt(resultEnum.getCode());
                }
            }
        }
        return 9999;
    }


    public static HttpEnum findBySubMsg(String subMsg) {
        if (StringUtils.isNotBlank(subMsg)) {
            for (HttpEnum resultEnum : values()) {
                if (subMsg.equals(resultEnum.getSubMsg())) {
                    return resultEnum;
                }
            }
        }
        return null;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public int getCodeToInt() {
        return Integer.valueOf(this.code);
    }

    @Override
    public Long getCodeToLong() {
        return Long.valueOf(this.code);
    }

    @Override
    public String getCodeToUpperCase() {
        return this.code.toUpperCase();
    }

    @Override
    public String getCodeToLowerCase() {
        return this.code.toLowerCase();
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

    @Override
    public String getMsgToUpperCase() {
        return this.msg.toUpperCase();
    }

    @Override
    public String getMsgToLowerCase() {
        return this.msg.toLowerCase();
    }

    @Override
    public String getSubMsg() {
        return this.subMsg;
    }
}
