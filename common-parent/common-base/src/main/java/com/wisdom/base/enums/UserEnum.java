package com.wisdom.base.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.wisdom.base.annotation.ResultFiled;
import org.apache.commons.lang3.StringUtils;

/**
 * @author captain
 * @version 1.0
 * @description 用户相关错误码
 * @dateTime 2018-05-04 13:35 星期五
 */

public enum UserEnum implements EnumDao {
    USER_ENUM_200("200", "成功"),

    USER_ENUM_9999("9999", "服务器内部异常");

    //    @JsonValue
    @EnumValue //mybatis_plus需要,若不需要可以删除
    @ResultFiled
    private String code;
    @ResultFiled
    private String msg;
    @ResultFiled
    private String subMsg;

    UserEnum() {
    }

    UserEnum(String code) {
        this.code = code;
    }

    UserEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    UserEnum(String code, String msg, String subMsg) {
        this.code = code;
        this.msg = msg;
        this.subMsg = subMsg;
    }

    public static UserEnum findByCode(String code) {
        if (StringUtils.isNotBlank(code)) {
            for (UserEnum value : values()) {
                if (code.equals(value.getCode())) {
                    return value;
                }
            }
        }
        return null;
    }

    public static UserEnum findByCode(long code) {
        return findByCode(String.valueOf(code));
    }

    public static UserEnum findByMsg(String msg) {
        if (StringUtils.isNotBlank(msg)) {
            for (UserEnum value : values()) {
                if (msg.equals(value.getMsg())) {
                    return value;
                }
            }
        }
        return null;
    }

    public static UserEnum findBySubMsg(String subMsg) {
        if (StringUtils.isNotBlank(subMsg)) {
            for (UserEnum value : values()) {
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
