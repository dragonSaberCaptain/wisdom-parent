package com.wisdom.auth.dao;

import com.wisdom.auth.entity.SysRole;
import com.wisdom.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统角色表 持久层
 *
 * @author captain
 * @version 1.0
 */
@Mapper
public interface SysRoleDaoExt extends BaseDao<SysRole> {

}
