package com.wisdom.openai.entity.embedding;

import com.wisdom.openai.entity.UsageEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 * 输入文本的嵌入向量 响应对象
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/3/23 13:28 星期四
 */
@Slf4j
@Data
@Accessors(chain = true)
public class EmbeddingRespEntity {
    /**
     * 生成嵌入向量的模型
     */
    private String model;

    /**
     * 返回的对象类型
     */
    private String object;

    /**
     * 嵌入向量的列表
     */
    private List<EmbeddingEntity> data;

    /**
     * 请求使用的OpenAI资源
     */
    private UsageEntity usageEntity;
}
