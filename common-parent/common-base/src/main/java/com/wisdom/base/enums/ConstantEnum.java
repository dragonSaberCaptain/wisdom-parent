package com.wisdom.base.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import org.apache.commons.lang3.StringUtils;

/**
 * @author captain
 * @version 1.0
 * @description 系统常量枚举类
 * @dateTime 2022/8/5 11:03 星期五
 */
public enum ConstantEnum implements EnumDao {
    DEFAULT_PORT("80"),
    DEFAULT_IP("127.0.0.1"),
    USER_AGENT("User-Agent"),
    LOCALHOST("localhost"),
    GET("GET"),
    POST("POST"),
    DELETE("DELETE");

    //    @JsonValue
    @EnumValue //mybatis_plus需要,若不需要可以删除
    private String code;
    private String msg;
    private String subMsg;

    ConstantEnum() {
    }

    ConstantEnum(String code) {
        this.code = code;
    }

    ConstantEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    ConstantEnum(String code, String msg, String subMsg) {
        this.code = code;
        this.msg = msg;
        this.subMsg = subMsg;
    }

    public static ConstantEnum findByCode(String code) {
        if (StringUtils.isNotBlank(code)) {
            for (ConstantEnum value : values()) {
                if (code.equals(value.getCode())) {
                    return value;
                }
            }
        }
        return null;
    }

    public static ConstantEnum findByCode(long code) {
        return findByCode(String.valueOf(code));
    }

    public static ConstantEnum findByMsg(String msg) {
        if (StringUtils.isNotBlank(msg)) {
            for (ConstantEnum value : values()) {
                if (msg.equals(value.getMsg())) {
                    return value;
                }
            }
        }
        return null;
    }

    public static ConstantEnum findBySubMsg(String subMsg) {
        if (StringUtils.isNotBlank(subMsg)) {
            for (ConstantEnum value : values()) {
                if (subMsg.equals(value.getSubMsg())) {
                    return value;
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
        return Integer.parseInt(this.code);
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
