package com.wisdom.auth.service.impl;

import com.wisdom.auth.entity.SysRolePermission;
import com.wisdom.auth.dao.SysRolePermissionDao;
import com.wisdom.auth.service.SysRolePermissionService;
import com.wisdom.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * Copyright ©2021 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 系统角色权限表 逻辑层
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2021-09-15 13:45:02 星期三
 */
@Slf4j
@Service
public class SysRolePermissionServiceImpl extends BaseServiceImpl<SysRolePermissionDao, SysRolePermission> implements SysRolePermissionService {
    @Resource
    private SysRolePermissionDao sysRolePermissionDao;

}
