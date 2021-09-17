package com.wisdom.auth.dao;

import com.wisdom.auth.entity.SysUserRole;
import com.wisdom.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * Copyright ©2021 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 系统用户角色表 持久层
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2021-09-15 13:45:02 星期三
 */
@Mapper
public interface SysUserRoleDao extends BaseDao<SysUserRole> {

}
