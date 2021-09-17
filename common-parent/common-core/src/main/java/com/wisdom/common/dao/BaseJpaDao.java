package com.wisdom.common.dao;

import com.wisdom.common.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * 继承jpd中JpaRepository的基础类
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/8/25 10:55 星期三
 */
public interface BaseJpaDao<T extends BaseEntity, M extends Serializable> extends JpaRepository<T, M> {
}
