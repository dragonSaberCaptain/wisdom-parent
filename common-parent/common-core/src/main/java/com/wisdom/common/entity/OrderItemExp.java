package com.wisdom.common.entity;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * TODO (用一句话描述该类的作用)
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/28 18:03 星期二
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OrderItem对象", description = "排序使用")
public class OrderItemExp extends OrderItem {
    @ApiModelProperty(value = "需要进行排序的字段")
    private String column;

    @ApiModelProperty(value = "是否正序排列，默认 true")
    private boolean asc = true;
}
