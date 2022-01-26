package com.wisdom.auth.service.impl;

import com.wisdom.auth.entity.SysRolePermissionExt;
import com.wisdom.auth.dao.SysRolePermissionDaoExt;
import com.wisdom.auth.service.SysRolePermissionServiceExt;
import com.wisdom.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统角色权限表 逻辑层
 *
 * @author captain
 * @version 1.0
 */
@Slf4j
@Service
public class SysRolePermissionServiceImplExt extends BaseServiceImpl<SysRolePermissionDaoExt, SysRolePermissionExt> implements SysRolePermissionServiceExt {
    @Autowired
    private SysRolePermissionDaoExt sysRolePermissionDaoExt;

}
