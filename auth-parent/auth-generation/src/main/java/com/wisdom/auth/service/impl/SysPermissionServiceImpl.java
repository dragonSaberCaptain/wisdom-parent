package com.wisdom.auth.service.impl;

import com.wisdom.auth.entity.SysPermission;
import com.wisdom.auth.dao.SysPermissionDao;
import com.wisdom.auth.service.SysPermissionService;
import com.wisdom.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
/**
 * Copyright ©2021 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 系统权限表 逻辑层
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2021-09-15 13:45:02 星期三
 */
@Slf4j
@Service
public class SysPermissionServiceImpl extends BaseServiceImpl<SysPermissionDao, SysPermission> implements SysPermissionService {
    @Autowired
    private SysPermissionDao sysPermissionDao;

}
