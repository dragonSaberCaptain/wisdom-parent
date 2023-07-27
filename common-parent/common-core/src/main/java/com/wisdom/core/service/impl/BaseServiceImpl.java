package com.wisdom.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisdom.core.dao.BaseDao;
import com.wisdom.core.entity.BaseEntity;
import com.wisdom.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @version 1.0
 * @description 通用实现类
 * @dateTime 2021/8/2 14:28 星期一
 */
public class BaseServiceImpl<M extends BaseDao<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T> {
    @Autowired
    private M baseDao;

    @Override
    public List<T> selectBySelective(Object object) {
        return baseDao.selectBySelective(object);
    }
}
