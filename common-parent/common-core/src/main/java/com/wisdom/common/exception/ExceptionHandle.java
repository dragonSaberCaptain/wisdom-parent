package com.wisdom.common.exception;

import com.alibaba.fastjson.JSONException;
import com.wisdom.config.dto.ResultDto;
import com.wisdom.config.enums.ResultEnum;
import com.wisdom.config.exception.ResultException;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Copyright © 2018 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @description 异常统一处理类
 * @date 2018-05-03 11:31 星期四
 */
@ControllerAdvice
public class ExceptionHandle {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultDto handle(Exception e) {
        if (e instanceof ResultException) {
            ResultException e1 = (ResultException) e;
            logger.error("【结果异常】", e1.fillInStackTrace());
            return new ResultDto(e1.getCode(), e1.getMsg(), e1.getSubMsg());
        }

        if (e instanceof JSONException) {
            JSONException e1 = (JSONException) e;
            logger.error("【json异常】", e1.fillInStackTrace());
            return new ResultDto(ResultEnum.JSON_ERROR);
        }

        if (e instanceof HttpMessageNotReadableException) {
            HttpMessageNotReadableException e1 = (HttpMessageNotReadableException) e;
            logger.error("【http内容不可读异常】", e1);
            return new ResultDto(ResultEnum.HTTP_MESSAGE_NOT_READABLE);
        }
        if (e instanceof NullPointerException) {
            NullPointerException e1 = (NullPointerException) e;
            logger.error("【空指针异常】", e1);
            return new ResultDto(ResultEnum.NULL_POINTER);
        }
        if (e instanceof ClassCastException) {
            ClassCastException e1 = (ClassCastException) e;
            logger.error("【类型转换异常】", e1);
            return new ResultDto(ResultEnum.CLASS_CAST);
        }
        if (e instanceof IndexOutOfBoundsException) {
            IndexOutOfBoundsException e1 = (IndexOutOfBoundsException) e;
            logger.error("【下标越界异常】", e1);
            return new ResultDto(ResultEnum.INDEX_OUT_OF_BOUNDS);
        }

        if (e instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException e1 = (MissingServletRequestParameterException) e;
            logger.error("【请求参数异常】", e1);
            return new ResultDto(ResultEnum.PARAMS_ERROR);
        }

        if (e instanceof NoSuchMethodException) {
            NoSuchMethodException e1 = (NoSuchMethodException) e;
            logger.error("【未知方法异常】", e1);
            return new ResultDto(ResultEnum.NO_SUCH_METHOD);
        }

        if (e instanceof FileNotFoundException) {
            FileNotFoundException e1 = (FileNotFoundException) e;
            logger.error("【文件路径异常】", e1);
            return new ResultDto(ResultEnum.FILE_PATH_ERROR);
        }

        if (e instanceof IOException) {
            IOException e1 = (IOException) e;
            logger.error("【IO异常】", e1);
            return new ResultDto(ResultEnum.IO_ABNORMAL);
        }

        if (e instanceof MyBatisSystemException) {
            MyBatisSystemException e1 = (MyBatisSystemException) e;
            logger.error("【数据异常】", e1);
            return new ResultDto(ResultEnum.NOT_UNIQUE_ERROR);
        }

        logger.error("【服务器内部异常】", e);
        return new ResultDto(ResultEnum.SERVER_INTERNAL_EXCEPTION);
    }
}
