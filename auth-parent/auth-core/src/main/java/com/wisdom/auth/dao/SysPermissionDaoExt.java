package com.wisdom.auth.dao;

import com.wisdom.auth.entity.SysPermission;
import com.wisdom.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Copyright ©2022 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 系统权限表 持久层
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2022-01-13 13:00:31 星期四
 */
@Mapper
public interface SysPermissionDaoExt extends BaseDao<SysPermission> {
    List<SysPermission> selectByUserId(Object object);
}
