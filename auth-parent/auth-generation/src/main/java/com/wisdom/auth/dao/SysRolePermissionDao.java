package com.wisdom.auth.dao;

import com.wisdom.auth.entity.SysRolePermission;
import com.wisdom.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * Copyright ©2021 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 系统角色权限表 持久层
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2021-09-15 13:45:02 星期三
 */
@Mapper
public interface SysRolePermissionDao extends BaseDao<SysRolePermission> {

}
