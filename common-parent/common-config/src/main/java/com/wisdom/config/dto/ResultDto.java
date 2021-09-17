package com.wisdom.config.dto;

import com.wisdom.config.enums.EnumDao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Copyright © 2018 dragonSaberCaptain. All rights reserved.
 *
 * @author dragonSaberCaptain
 * @description 统一封装返回对象
 * @date 2018-05-04 13:34 星期五
 */
@Data
@ApiModel(value = "ResultDto 响应对象", description = "统一响应对象")
public class ResultDto<T> implements Serializable {
    @ApiModelProperty(value = "状态码")
    protected String code;
    @ApiModelProperty(value = "状态码描述")
    protected String msg;
    @ApiModelProperty(value = "状态码附加描述")
    protected String subMsg;
    @ApiModelProperty(value = "数据结果")
    protected T data;

    public ResultDto(Object object) {
        if (object instanceof EnumDao) {
            EnumDao enumInterface = (EnumDao) object;
            this.code = enumInterface.getCode();
            this.msg = enumInterface.getMsg();
            this.subMsg = enumInterface.getSubMsg();
        }
    }

    public ResultDto(Object object, T data) {
        if (object instanceof EnumDao) {
            EnumDao enumInterface = (EnumDao) object;
            this.code = enumInterface.getCode();
            this.msg = enumInterface.getMsg();
            this.subMsg = enumInterface.getSubMsg();
            this.data = data;
        }
    }

    public ResultDto(Object object, String subMsg, T data) {
        if (object instanceof EnumDao) {
            EnumDao enumInterface = (EnumDao) object;
            this.code = enumInterface.getCode();
            this.msg = enumInterface.getMsg();
            this.subMsg = subMsg;
            this.data = data;
        }
    }

    public ResultDto(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultDto(String code, String msg, String subMsg) {
        this.code = code;
        this.msg = msg;
        this.subMsg = subMsg;
    }
}