package com.wisdom.config.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import org.apache.commons.lang3.StringUtils;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @version 1.0
 * @description 字符编码枚举类
 * @date 2021/7/5 10:00 星期一
 */
public enum CharEncodingEnum implements EnumDao {
    // ****************************【常用的字符编码】****************************
    UTF_8("UTF-8"),

    /**
     * 扩展GB2312
     */
    GBK("GBK"),

    /**
     * 扩展ASCII编码
     */
    ISO_8859_1("ISO-8859-1"),

    US_ASCII("US-ASCII"),

    UTF_16("UTF-16"),

    GB2312("GB2312"),

    /**
     * 兼容GB2312
     */
    GB18030("GB18030"),

    UTF_16BE("UTF-16BE"),

    UTF_16LE("UTF-16LE");

    //    @JsonValue
    @EnumValue //mybatis_plus需要,若不需要可以删除
    private String code;
    private String msg;
    private String subMsg;

    CharEncodingEnum() {
    }

    CharEncodingEnum(String code) {
        this.code = code;
    }

    CharEncodingEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    CharEncodingEnum(String code, String msg, String subMsg) {
        this.code = code;
        this.msg = msg;
        this.subMsg = subMsg;
    }

    public static CharEncodingEnum findByCode(String code) {
        if (StringUtils.isNotBlank(code)) {
            for (CharEncodingEnum resultEnum : values()) {
                if (code.equals(resultEnum.getCode())) {
                    return resultEnum;
                }
            }
        }
        return null;
    }

    public static CharEncodingEnum findByCode(long code) {
        return findByCode(String.valueOf(code));
    }

    public static CharEncodingEnum findByMsg(String msg) {
        if (StringUtils.isNotBlank(msg)) {
            for (CharEncodingEnum resultEnum : values()) {
                if (msg.equals(resultEnum.getMsg())) {
                    return resultEnum;
                }
            }
        }
        return null;
    }

    public static CharEncodingEnum findBySubMsg(String subMsg) {
        if (StringUtils.isNotBlank(subMsg)) {
            for (CharEncodingEnum resultEnum : values()) {
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
