package com.wisdom.openai.entity.edit;

import com.wisdom.openai.entity.UsageEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Openai生成的编辑列表响应对象
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/3/22 13:29 星期三
 */
@Slf4j
@Data
@Accessors(chain = true)
public class EditRespEntity {
    /**
     * 返回的对象类型
     */
    private String object;

    /**
     * 创建时间（以epoch秒为单位）
     */
    private long created;

    /**
     * 生成的编辑列表
     */
    private List<EditChoiceEntity> choices;

    /**
     * 请求使用的OpenAI资源情况
     */
    private UsageEntity usage;
}
