package com.wisdom.auth.service.impl;

import com.wisdom.auth.entity.SysRolePermission;
import com.wisdom.auth.dao.SysRolePermissionDao;
import com.wisdom.auth.service.SysRolePermissionService;
import com.wisdom.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

/**
 * Copyright ©2021 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 系统角色权限表 逻辑层
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2021-09-28 13:08:29 星期二
 */
@Slf4j
@Service
public class SysRolePermissionServiceImpl extends BaseServiceImpl<SysRolePermissionDao, SysRolePermission> implements SysRolePermissionService {
    @Autowired
    private SysRolePermissionDao sysRolePermissionDao;

}
