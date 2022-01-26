package com.wisdom.example.dao;

import com.wisdom.example.entity.User;
import com.wisdom.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * Copyright ©2022 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 用户表 持久层
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2022-01-04 13:44:06 星期二
 */
@Mapper
public interface UserDao extends BaseDao<User> {

}
