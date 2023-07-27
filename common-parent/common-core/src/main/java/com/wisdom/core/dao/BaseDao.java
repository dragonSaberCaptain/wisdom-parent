package com.wisdom.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisdom.core.entity.BaseEntity;

import java.util.List;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * 继承mybatisplus中BaseMapper的基础类
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/8/25 11:03 星期三
 */
public interface BaseDao<T extends BaseEntity> extends BaseMapper<T> {
    List<T> selectBySelective(Object object);
}
