package com.wisdom.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wisdom.core.entity.BaseEntity;

import java.util.List;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @version 1.0
 * @description 通用接口
 * @dateTime 2021/8/2 14:27 星期一
 */
public interface BaseService<T extends BaseEntity> extends IService<T> {
    List<T> selectBySelective(Object object);
}
