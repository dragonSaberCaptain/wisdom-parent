package com.wisdom.openai.entity.completions.chat;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * openai 聊天模型要为生成聊天完成的消息
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/3/22 13:55 星期三
 */
@Slf4j
@Data
@Accessors(chain = true)
public class ChatMessageEntity {
    /**
     * 使用角色 范围:system、user、assistant
     *
     * @mock user
     * see {@link ChatMessageRoleEnum}
     */
    String role;
    /**
     * 内容
     */
    String content;
}
