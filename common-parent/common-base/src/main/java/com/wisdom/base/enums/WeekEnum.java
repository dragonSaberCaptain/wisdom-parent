package com.wisdom.base.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import org.apache.commons.lang3.StringUtils;

/**
 * @author captain
 * @version 1.0
 * @description 星期相关枚举类
 * @dateTime 2021/7/2 10:23 星期五
 */
public enum WeekEnum implements EnumDao {
    // ****************************【常用的星期】****************************
    MONDAY("monday", "星期一"),

    TUESDAY("tuesday", "星期二"),

    WEDNESDAY("wednesday", "星期三"),

    THURSDAY("thursday", "星期四"),

    FRIDAY("friday", "星期五"),

    SATURDAY("saturday", "星期六"),

    SUNDAY("sunday", "星期日"),

    UNKNOWN("unknown", "未知");

    //    @JsonValue
    @EnumValue //mybatis_plus需要,若不需要可以删除
    private String code;
    private String msg;
    private String subMsg;

    WeekEnum() {
    }

    WeekEnum(String code) {
        this.code = code;
    }

    WeekEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    WeekEnum(String code, String msg, String subMsg) {
        this.code = code;
        this.msg = msg;
        this.subMsg = subMsg;
    }

    public static WeekEnum findByCode(String code) {
        if (StringUtils.isNotBlank(code)) {
            for (var value : values()) {
                if (code.equalsIgnoreCase(value.getCode())) {
                    return value;
                }
            }
        }
        return null;
    }

    public static WeekEnum findByCode(long code) {
        return findByCode(String.valueOf(code));
    }

    public static WeekEnum findByMsg(String msg) {
        if (StringUtils.isNotBlank(msg)) {
            for (var value : values()) {
                if (msg.equalsIgnoreCase(value.getMsg())) {
                    return value;
                }
            }
        }
        return null;
    }

    public static WeekEnum findBySubMsg(String subMsg) {
        if (StringUtils.isNotBlank(subMsg)) {
            for (WeekEnum resultEnum : values()) {
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
        return Integer.parseInt(this.code);
    }

    @Override
    public Long getCodeToLong() {
        return Long.valueOf(this.code);
    }

    @Override
    public String getMsg() {
        return this.msg;
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
