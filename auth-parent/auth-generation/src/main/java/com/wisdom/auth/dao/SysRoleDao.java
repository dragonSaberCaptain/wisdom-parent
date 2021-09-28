package com.wisdom.auth.dao;

import com.wisdom.auth.entity.SysRole;
import com.wisdom.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * Copyright ©2021 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 系统角色表 持久层
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2021-09-28 13:08:29 星期二
 */
@Mapper
public interface SysRoleDao extends BaseDao<SysRole> {

}
