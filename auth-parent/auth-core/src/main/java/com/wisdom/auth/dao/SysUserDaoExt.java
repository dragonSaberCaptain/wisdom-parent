package com.wisdom.auth.dao;

import com.wisdom.auth.entity.SysUser;
import com.wisdom.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户表 持久层
 *
 * @author captain
 * @version 1.0
 */
@Mapper
public interface SysUserDaoExt extends BaseDao<SysUser> {

}
