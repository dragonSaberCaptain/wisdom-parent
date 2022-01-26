package com.wisdom.example.dao;

import com.wisdom.example.entity.User;
import com.wisdom.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表 持久层
 *
 * @author captain
 * @version 1.0
 */
@Mapper
public interface UserDao extends BaseDao<User> {

}
