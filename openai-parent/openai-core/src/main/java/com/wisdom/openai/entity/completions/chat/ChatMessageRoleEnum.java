package com.wisdom.openai.entity.completions.chat;

/**
 * openai 聊天模型 角色枚举类
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/3/22 13:55 星期三
 */
public enum ChatMessageRoleEnum {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant");

    private final String value;

    ChatMessageRoleEnum(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
