package com.wisdom.config.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.wisdom.config.annotation.ResultFiled;
import org.apache.commons.lang3.StringUtils;

/**
 * Copyright © 2018 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @description 系统灾害级别分为：一级响应，二级响应，三级响应，四级响应
 * @date 2018-05-04 13:35 星期五
 */
public enum ResultEnum implements EnumDao {
    // ****************************【公共错误】****************************
    OK("200", "成功"),

    FAIL("1002", "失败", "fail"),

    // ****************************【文件错误】****************************
    FILE_NOT_EMPTY("1004", "文件不能为空", "the file cannot be empty"),

    FILE_TOO_BIG("1005", "文件太大了", "the file is too big"),
    // ****************************【一级错误】****************************
    NOT_UNIQUE_ERROR("9001", "数据不唯一", "data not unique error"),

    REDIS_NO_OPEN("9002", "服务器缓存错误", "server cache error"),

    UPDATE_FAIL("9003", "更新失败"),

    ADD_FAIL("9004", "新增失败"),

    DELETE_FAIL("9005", "删除失败"),

    PARAMS_ERROR("9006", "参数错误，请确认格式是否正确"),

    IO_ABNORMAL("9007", "IO异常"),

    HTTP_MESSAGE_NOT_READABLE("9008", "http内容不可读异常"),

    JSON_ERROR("9009", "json异常"),

    NULL_POINTER("9010", "空指针异常"),

    CLASS_CAST("9011", "类型转换异常"),

    NO_SUCH_METHOD("9012", "未知方法异常"),

    INDEX_OUT_OF_BOUNDS("9013", "下标越界异常"),

    FILE_PATH_ERROR("9014", "文件路径错误"),

    TOKEN_IS_EMPTY("9015", "token不能为空"),

    DATA_TAMPERED("9016", "数据安全错误,数据被篡改"),

    BAN_REQUEST("9017", "已限流,禁止请求"),

    SERVER_INTERNAL_EXCEPTION("9999", "服务器内部异常");

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
