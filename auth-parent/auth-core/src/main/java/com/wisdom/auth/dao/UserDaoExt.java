package com.wisdom.auth.dao;

import com.wisdom.auth.entity.UserExt;
import com.wisdom.core.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表 持久层
 *
 * @author captain
 * @version 1.0.0
 */
@Mapper
public interface UserDaoExt extends BaseDao<UserExt> {

}
