package com.wisdom.config.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import org.apache.commons.lang3.StringUtils;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 *
 * 日期时间枚举类
 *
 * @author captain
 * @version 1.0
 * @date 2021/6/30 14:42 星期三
 */
public enum DateTimeEnum implements EnumDao {
    // ****************************【常用的日期时间格式】****************************
    DATETIME_PATTERN("yyyy-MM-dd HH:mm:ss", "yyyy年MM月dd日 HH时mm分ss秒"),
    DATETIME_PATTERN_UN("yyyyMMddHHmmss", "yyyy年MM月dd日HH时mm分ss秒"),

    DATETIME_PATTERN_MILLI("yyyy-MM-dd HH:mm:ss:SSS", "yyyy年MM月dd日 HH时mm分ss秒SSS毫秒"),
    DATETIME_PATTERN_MILLI_UN("yyyyMMddHHmmssSSS", "yyyy年MM月dd日HH时mm分ss秒SSS毫秒"),

    DATE_PATTERN("yyyy-MM-dd", "yyyy年MM月dd日"),
    DATE_PATTERN_UN("yyyyMMdd", "yyyy年MM月dd日"),

    TIME_PATTERN("HH:mm:ss", "HH时mm分ss秒"),
    TIME_PATTERNE_MILLI("HH:mm:ss:SSS", "HH时mm分ss秒SSS毫秒");
    //    @JsonValue
    @EnumValue //mybatis_plus需要,若不需要可以删除
    private String code;
    private String msg;
    private String subMsg;

    DateTimeEnum() {
    }

    DateTimeEnum(String code) {
        this.code = code;
    }

    DateTimeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    DateTimeEnum(String code, String msg, String subMsg) {
        this.code = code;
        this.msg = msg;
        this.subMsg = subMsg;
    }

    public static DateTimeEnum findByCode(String code) {
        if (StringUtils.isNotBlank(code)) {
            for (DateTimeEnum resultEnum : values()) {
                if (code.equals(resultEnum.getCode())) {
                    return resultEnum;
                }
            }
        }
        return null;
    }

    public static DateTimeEnum findByCode(long code) {
        return findByCode(String.valueOf(code));
    }

    public static DateTimeEnum findByMsg(String msg) {
        if (StringUtils.isNotBlank(msg)) {
            for (DateTimeEnum resultEnum : values()) {
                if (msg.equals(resultEnum.getMsg())) {
                    return resultEnum;
                }
            }
        }
        return null;
    }

    public static DateTimeEnum findBySubMsg(String subMsg) {
        if (StringUtils.isNotBlank(subMsg)) {
            for (DateTimeEnum resultEnum : values()) {
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
