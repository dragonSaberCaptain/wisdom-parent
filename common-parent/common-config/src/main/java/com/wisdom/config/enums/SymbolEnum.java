package com.wisdom.config.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import org.apache.commons.lang3.StringUtils;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @version 1.0
 * @description 符号相关枚举
 * @date 2021/7/5 14:14 星期一
 */
public enum SymbolEnum implements EnumDao {
    // ****************************【常用的符号】****************************
    STAR("*", "星号", "asterisk"),

    STUB("-", "短线", "stub"),

    UNDERLINE("_", "下划线", "underline"),

    QUESTION_MARK("?", "问号", "question mark"),

    AND("&", "与", "and"),

    EQUAL_MARK("=", "等号", "equal mark"),

    DOT(".", "点", "dot"),

    FORWARD_SLASH("/", "正斜杠", "forward slash"),

    BACKSLASH("\\", "反斜杠", "backslash"),

    LEFT_BRACKETS("(", "左括号", "left brackets"),

    RIGHT_BRACKETS(")", "右括号", "right brackets"),

    LEFT_CENTRE_BRACKETS("[", "左中括号", "left centre brackets"),

    RIGHT_CENTRE_BRACKETS("]", "右中括号", "right centre brackets"),

    LEFT_LARGE_BRACKETS("{", "左中括号", "left large brackets"),

    RIGHT_LARGE_BRACKETS("}", "右中括号", "right large brackets"),

    EMPTY("", "空", "empty");

    //    @JsonValue
    @EnumValue //mybatis_plus需要,若不需要可以删除
    private String code;
    private String msg;
    private String subMsg;

    SymbolEnum() {
    }

    SymbolEnum(String code) {
        this.code = code;
    }

    SymbolEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    SymbolEnum(String code, String msg, String subMsg) {
        this.code = code;
        this.msg = msg;
        this.subMsg = subMsg;
    }

    public static SymbolEnum findByCode(String code) {
        if (StringUtils.isNotBlank(code)) {
            for (SymbolEnum resultEnum : values()) {
                if (code.equals(resultEnum.getCode())) {
                    return resultEnum;
                }
            }
        }
        return null;
    }

    public static SymbolEnum findByCode(long code) {
        return findByCode(String.valueOf(code));
    }

    public static SymbolEnum findByMsg(String msg) {
        if (StringUtils.isNotBlank(msg)) {
            for (SymbolEnum resultEnum : values()) {
                if (msg.equals(resultEnum.getMsg())) {
                    return resultEnum;
                }
            }
        }
        return null;
    }

    public static SymbolEnum findBySubMsg(String subMsg) {
        if (StringUtils.isNotBlank(subMsg)) {
            for (SymbolEnum resultEnum : values()) {
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
