package com.wisdom.openai.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 请求使用的OpenAI资源情况
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/3/22 13:29 星期三
 */
@Slf4j
@Data
@Accessors(chain = true)
public class UsageEntity {
    /**
     * 使用的提示标记数
     */
    private Integer promptTokens;
    /**
     * 使用的完成标记数
     */
    private Integer completionTokens;
    /**
     * 使用的令牌总数
     */
    private Integer totalTokens;
}
