package com.wisdom.openai.entity.completions;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * openai 自定义模型响应对象 完成项列表
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/3/22 13:29 星期三
 */
@Slf4j
@Data
@Accessors(chain = true)
public class ChoicesEntity {
    /**
     * 生成的文本。如果echo参数为true，将包含提示
     */
    private String text;
    /**
     * 返回列表中此完成的索引
     */
    private Integer index;
    /**
     * 所选令牌和顶部logprobs参数令牌的日志概率
     */
    private Object logprobs;
    /**
     * 停止生成的原因
     */
    private String finishReason;
}
