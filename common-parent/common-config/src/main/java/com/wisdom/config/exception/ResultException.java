package com.wisdom.config.exception;

import com.wisdom.config.enums.EnumDao;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @version 1.0
 * @description 自定义异常类
 * @date 2021/7/6 17:09 星期二
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResultException extends RuntimeException {
    protected String code;
    protected String msg;
    protected String subMsg;

    public ResultException(Object object) {
        if (object instanceof EnumDao) {
            EnumDao enumInterface = (EnumDao) object;
            this.code = enumInterface.getCode();
            this.msg = enumInterface.getMsg();
            this.subMsg = enumInterface.getSubMsg();
        }
    }
}
