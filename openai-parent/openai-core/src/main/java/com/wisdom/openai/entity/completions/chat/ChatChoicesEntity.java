package com.wisdom.openai.entity.completions.chat;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * openai 聊天模型响应对象 完成项列表
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/3/22 13:55 星期三
 */
@Slf4j
@Data
@Accessors(chain = true)
public class ChatChoicesEntity {
    /**
     * 要为生成聊天完成的消息
     */
    private ChatMessageEntity message;
    /**
     * 停止生成的原因
     */
    private String finishReason;
    /**
     * 返回列表中此完成的索引
     */
    private Integer index;
}
