package com.wisdom.base.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wisdom.base.enums.EnumDao;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @version 1.0
 * @description 自定义异常类
 * @dateTime 2021/7/6 17:09 星期二
 */
@Data
@Slf4j
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultException extends RuntimeException {
    protected String code;
    protected String msg;
    protected String subMsg;
    protected Object data;

    public ResultException(Object object) {
        if (object instanceof EnumDao enumInterface) {
            this.code = enumInterface.getCode();
            this.msg = enumInterface.getMsg();
            this.subMsg = enumInterface.getSubMsg();
        }
    }

    public ResultException(Object object, Object entity) {
        if (object instanceof EnumDao) {
            EnumDao enumInterface = (EnumDao) object;
            this.code = enumInterface.getCode();
            this.msg = enumInterface.getMsg();
            this.subMsg = enumInterface.getSubMsg();
            this.data = entity;
        }
    }

    public ResultException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultException(String code, String msg, String subMsg) {
        this.code = code;
        this.msg = msg;
        this.subMsg = subMsg;
    }
}
