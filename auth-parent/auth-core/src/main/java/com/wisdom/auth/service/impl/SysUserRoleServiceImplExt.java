package com.wisdom.auth.service.impl;

import com.wisdom.auth.entity.SysUserRole;
import com.wisdom.auth.dao.SysUserRoleDaoExt;
import com.wisdom.auth.service.SysUserRoleServiceExt;
import com.wisdom.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统用户角色表 逻辑层
 *
 * @author captain
 * @version 1.0
 */
@Slf4j
@Service
public class SysUserRoleServiceImplExt extends BaseServiceImpl<SysUserRoleDaoExt, SysUserRole> implements SysUserRoleServiceExt {
    @Autowired
    private SysUserRoleDaoExt sysUserRoleDaoExt;

}
