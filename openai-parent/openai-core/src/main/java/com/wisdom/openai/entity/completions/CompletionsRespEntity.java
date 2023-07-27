package com.wisdom.openai.entity.completions;

import com.wisdom.openai.entity.UsageEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 * openai 自定义模型响应对象
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/3/22 13:29 星期三
 */
@Slf4j
@Data
@Accessors(chain = true)
public class CompletionsRespEntity {
    /**
     * 分配给此完成的唯一id
     */
    private String id;
    /**
     * 返回的对象类型
     */
    private String object;
    /**
     * 创建时间（以epoch秒为单位）
     */
    private Long created;
    /**
     * 所使用的模型
     */
    private String model;
    /**
     * 生成的完成项列表
     */
    private List<ChoicesEntity> choices;
    /**
     * 请求使用的OpenAI资源情况
     */
    private UsageEntity usage;
}