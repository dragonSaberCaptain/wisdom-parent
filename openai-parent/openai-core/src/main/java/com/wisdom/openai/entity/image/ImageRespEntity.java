package com.wisdom.openai.entity.image;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author captain
 * @version 1.0
 * @description 图片模型返回对象
 * @className ImageRespEntity
 * @projectName wisdom-parent
 * @packageName com.wisdom.openai.entity.image
 * @datetime 2023/4/11 11:09 星期二
 */
@Slf4j
@Data
@Accessors(chain = true)
public class ImageRespEntity {
    /**
     * 创建时间（以epoch秒为单位）
     */
    private Long createdAt;

    /**
     * 图像结果列表
     */
    private List<ImageEntity> data;
}
