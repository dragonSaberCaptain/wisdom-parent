package com.wisdom.auth.dao;

import com.wisdom.auth.entity.SysUserRole;
import com.wisdom.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户角色表 持久层
 *
 * @author captain
 * @version 1.0.0
 */
@Mapper
public interface SysUserRoleDao extends BaseDao<SysUserRole> {

}
