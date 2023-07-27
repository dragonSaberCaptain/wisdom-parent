package com.wisdom.openai.entity.edit;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * 生成的编辑对象
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/3/22 13:29 星期三
 */
@Slf4j
@Data
@Accessors(chain = true)
public class EditChoiceEntity {
    /**
     * 编辑后的文本
     */
    private String text;
    /**
     * 返回列表中此完成的索引
     */
    private Integer index;
}
