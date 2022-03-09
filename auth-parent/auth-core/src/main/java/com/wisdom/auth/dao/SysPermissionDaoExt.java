package com.wisdom.auth.dao;

import com.wisdom.auth.entity.SysPermission;
import com.wisdom.auth.entity.SysPermissionExt;
import com.wisdom.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统权限表 持久层
 *
 * @author captain
 * @version 1.0
 */
@Mapper
public interface SysPermissionDaoExt extends BaseDao<SysPermissionExt> {
    List<SysPermission> selectByUserId(Object object);
}
