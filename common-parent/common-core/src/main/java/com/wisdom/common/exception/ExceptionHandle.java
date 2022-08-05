package com.wisdom.common.exception;

import com.wisdom.config.dto.ResultDto;
import com.wisdom.config.enums.ResultEnum;
import com.wisdom.config.exception.ResultException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright © 2018 dragonSaberCaptain. All rights reserved.
 * 异常统一处理类
 *
 * @author captain
 * @date 2018-05-03 11:31 星期四
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandle {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultDto<T> handle(Exception e) {
        if (e instanceof ResultException) {
            ResultException e1 = (ResultException) e;
            log.error("【返回结果异常】", e1.fillInStackTrace());
            return new ResultDto<>(e1.getCode(), e1.getMsg(), e1.getSubMsg());
        }

        log.error("【服务器内部异常】", e);
        return new ResultDto<>(ResultEnum.RESULT_ENUM_9999);
    }
}
