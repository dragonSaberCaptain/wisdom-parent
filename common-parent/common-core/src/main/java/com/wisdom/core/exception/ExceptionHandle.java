package com.wisdom.core.exception;

import cn.hutool.json.JSONUtil;
import com.wisdom.base.dto.ResultDto;
import com.wisdom.base.enums.ResultEnum;
import com.wisdom.base.exception.ResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright © 2018 dragonSaberCaptain. All rights reserved.
 * 异常统一处理类
 *
 * @author captain
 * @dateTime 2018-05-03 11:31 星期四
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandle {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultDto<Object> handle(Exception e) {
        if (e instanceof ResultException e1) {
            ResultDto<Object> resultData = new ResultDto<>(e1);
            log.error("【返回结果异常】:{}", JSONUtil.toJsonStr(resultData));
            return resultData;
        }

        log.error("【服务器内部异常】", e);
        return new ResultDto<>(ResultEnum.RESULT_ENUM_9999);
    }
}
