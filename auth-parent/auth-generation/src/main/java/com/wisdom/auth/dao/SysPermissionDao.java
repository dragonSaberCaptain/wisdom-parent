package com.wisdom.auth.dao;

import com.wisdom.auth.entity.SysPermission;
import com.wisdom.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统权限表 持久层
 *
 * @author captain
 * @version 1.0.0
 */
@Mapper
public interface SysPermissionDao extends BaseDao<SysPermission> {

}
