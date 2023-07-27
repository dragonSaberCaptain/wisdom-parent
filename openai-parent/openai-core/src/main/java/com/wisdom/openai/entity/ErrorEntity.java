package com.wisdom.openai.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * 表示OpenAI请求失败时的错误主体
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/3/23 11:05 星期四
 */
@Slf4j
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorEntity {
    /**
     * 错误消息描述
     */
    public String message;
    /**
     * OpenAI错误类型
     */
    public String type;
    /**
     * 参数
     */
    public String param;
    /**
     * 错误代码
     */
    public String code;
}
