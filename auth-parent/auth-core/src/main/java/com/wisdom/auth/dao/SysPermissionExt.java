package com.wisdom.auth.dao;

import com.wisdom.auth.entity.SysPermission;
import com.wisdom.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Copyright ©2021 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 持久层
 * </p>
 *
 * @author captain
 * @version 1.0
 * @createDate 2021-09-13 15:34:22 星期一
 */
@Mapper
public interface SysPermissionExt extends BaseDao<SysPermission> {
    List<SysPermission> selectByUserId(Object object);
}
