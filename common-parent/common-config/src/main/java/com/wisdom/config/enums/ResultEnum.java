package com.wisdom.config.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.wisdom.config.annotation.ResultFiled;
import org.apache.commons.lang3.StringUtils;

/**
 * Copyright © 2018 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @date 2018-05-04 13:35 星期五
 */
public enum ResultEnum implements EnumDao {
    RESULT_ENUM_200("200", "成功"),

    RESULT_ENUM_1002("1002", "失败", "fail"),

    RESULT_ENUM_1003("1003", "参数错误，拒绝访问"),

    RESULT_ENUM_1004("1004", "更新失败"),

    RESULT_ENUM_1005("1005", "新增失败"),

    RESULT_ENUM_1006("1006", "删除失败"),

    RESULT_ENUM_1007("1007", "数据安全错误,数据被篡改"),

    RESULT_ENUM_1008("1008", "token不能为空"),

    RESULT_ENUM_1009("1009", "已限流,禁止请求"),

    RESULT_ENUM_1010("1010", "数据不是唯一的", "data not unique error"),

    RESULT_ENUM_1011("1011", "数据不存在", "data does not exist"),

    RESULT_ENUM_1012("1012", "请求头不能为空"),

    RESULT_ENUM_1013("1013", "文件不能为空", "the file cannot be empty"),

    RESULT_ENUM_1014("1014", "文件太大了", "the file is too big"),

    RESULT_ENUM_1023("1023", "文件路径异常"),

    RESULT_ENUM_9999("9999", "服务器内部异常");

    //    @JsonValue
    @EnumValue //mybatis_plus需要,若不需要可以删除
    @ResultFiled
    private String code;
    @ResultFiled
    private String msg;
    @ResultFiled
    private String subMsg;

    ResultEnum() {
    }

    ResultEnum(String code) {
        this.code = code;
    }

    ResultEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    ResultEnum(String code, String msg, String subMsg) {
        this.code = code;
        this.msg = msg;
        this.subMsg = subMsg;
    }

    public static ResultEnum findByCode(String code) {
        if (StringUtils.isNotBlank(code)) {
            for (ResultEnum resultEnum : values()) {
                if (code.equals(resultEnum.getCode())) {
                    return resultEnum;
                }
            }
        }
        return null;
    }

    public static ResultEnum findByCode(long code) {
        return findByCode(String.valueOf(code));
    }

    public static ResultEnum findByMsg(String msg) {
        if (StringUtils.isNotBlank(msg)) {
            for (ResultEnum resultEnum : values()) {
                if (msg.equals(resultEnum.getMsg())) {
                    return resultEnum;
                }
            }
        }
        return null;
    }

    public static ResultEnum findBySubMsg(String subMsg) {
        if (StringUtils.isNotBlank(subMsg)) {
            for (ResultEnum resultEnum : values()) {
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
