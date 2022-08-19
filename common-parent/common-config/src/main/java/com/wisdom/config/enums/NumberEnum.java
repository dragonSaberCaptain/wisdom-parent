package com.wisdom.config.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import org.apache.commons.lang3.StringUtils;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @version 1.0
 * @description 数字相关枚举
 * @date 2021/7/2 9:45 星期五
 */
public enum NumberEnum implements EnumDao {
    // ****************************【常用的数字】****************************
    MINUS_ONE("-1", "负一", "minus one"),

    ZERO("0", "零", "zero"),

    ONE("1", "一", "one"),

    TWO("2", "二", "two"),

    THREE("3", "三", "three"),

    FOUR("4", "四", "four"),

    FIVE("5", "五", "five"),

    SIX("6", "六", "six"),

    SEVEN("7", "七", "seven"),

    EIGHT("8", "八", "eight"),

    NINE("9", "九", "nine"),

    TEN("10", "十", "ten");

    //    @JsonValue
    @EnumValue //mybatis_plus需要,若不需要可以删除
    private String code;
    private String msg;
    private String subMsg;

    NumberEnum() {
    }

    NumberEnum(String code) {
        this.code = code;
    }

    NumberEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    NumberEnum(String code, String msg, String subMsg) {
        this.code = code;
        this.msg = msg;
        this.subMsg = subMsg;
    }

    public static NumberEnum findByCode(String code) {
        if (StringUtils.isNotBlank(code)) {
            for (var value : values()) {
                if (code.equals(value.getCode())) {
                    return value;
                }
            }
        }
        return null;
    }

    public static NumberEnum findByCode(long code) {
        return findByCode(String.valueOf(code));
    }

    public static NumberEnum findByMsg(String msg) {
        if (StringUtils.isNotBlank(msg)) {
            for (var value : values()) {
                if (msg.equals(value.getMsg())) {
                    return value;
                }
            }
        }
        return null;
    }

    public static NumberEnum findBySubMsg(String subMsg) {
        if (StringUtils.isNotBlank(subMsg)) {
            for (var value : values()) {
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
