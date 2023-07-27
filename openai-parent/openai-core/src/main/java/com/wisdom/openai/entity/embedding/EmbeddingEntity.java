package com.wisdom.openai.entity.embedding;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 * 嵌入向量对象
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/3/23 14:19 星期四
 */
@Slf4j
@Data
@Accessors(chain = true)
public class EmbeddingEntity {
    /**
     * 返回的对象类型
     */
    private String object;

    /**
     * 向量值
     */
    private List<Double> embedding;

    /**
     * 此嵌入在列表中的位置
     */
    private Integer index;
}
