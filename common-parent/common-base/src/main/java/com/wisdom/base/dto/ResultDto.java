package com.wisdom.base.dto;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wisdom.base.enums.EnumDao;
import com.wisdom.base.exception.ResultException;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Map;

/**
 * @author dragonSaberCaptain
 * @description 统一封装返回对象
 * @dateTime 2018-05-04 13:34 星期五
 */
@Data
@Slf4j
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultDto<T> implements Serializable {
    /**
     * 响应码
     *
     * @mock 200
     */
    protected String code;
    /**
     * 响应码描述
     *
     * @mock 成功
     */
    protected String msg;
    /**
     * 响应码的子描述
     *
     * @mock 成功
     */
    protected String subMsg;
    /**
     * 数据
     */
    protected T data;

    public ResultDto(Object object) {
        if (object instanceof EnumDao enumInterface) {
            this.code = enumInterface.getCode();
            this.msg = enumInterface.getMsg();
            this.subMsg = enumInterface.getSubMsg();
        } else if (object instanceof ResultException e1) {
            this.code = e1.getCode();
            this.msg = e1.getMsg();
            this.subMsg = e1.getSubMsg();
            this.data = (T) e1.getData();
        }
    }

    public ResultDto(Object object, T data) {
        if (object instanceof EnumDao enumInterface) {
            this.code = enumInterface.getCode();
            this.msg = enumInterface.getMsg();
            this.subMsg = enumInterface.getSubMsg();
            this.data = data;
        }
    }

    public ResultDto(Object object, String subMsg, T data) {
        if (object instanceof EnumDao enumInterface) {
            this.code = enumInterface.getCode();
            this.msg = enumInterface.getMsg();
            this.subMsg = subMsg;
            this.data = data;
        }
    }

    public ResultDto(Object object, String dataJson) {
        if (object instanceof EnumDao enumInterface) {
            this.code = enumInterface.getCode();
            this.msg = enumInterface.getMsg();
            this.subMsg = enumInterface.getSubMsg();
            try {
                this.data = (T) JSONUtil.toBean(dataJson, Map.class);
            } catch (Exception e) {
                this.data = (T) dataJson;
            }
        } else if (object instanceof String) {
            this.code = (String) object;
            this.msg = dataJson;
        }
    }

    public ResultDto(String code, String msg, String subMsg) {
        this.code = code;
        this.msg = msg;
        this.subMsg = subMsg;
    }

    public ResultDto(String code, String msg, String subMsg, T data) {
        this.code = code;
        this.msg = msg;
        this.subMsg = subMsg;
        this.data = data;
    }
}