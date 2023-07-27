package com.wisdom.openai.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * openai 返回值包装类
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/3/23 11:04 星期四
 */
@Slf4j
@Data
@Accessors(chain = true)
public class OpenAiRespEntity<T> {
    /**
     * 返回的对象类型
     */
    private String object;
    /**
     * 包含实际结果的列表
     */
    private List<T> data;
}
