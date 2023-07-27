package com.wisdom.openai.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * openai 错误响应对象封装
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/3/23 11:04 星期四
 */
@Slf4j
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OpenaiErrorRespEntity {
    /**
     * 错误
     */
    public ErrorEntity error;
}
