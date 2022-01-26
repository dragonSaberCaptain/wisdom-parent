package com.wisdom.auth.service.impl;

import com.wisdom.auth.entity.SysRoleExt;
import com.wisdom.auth.dao.SysRoleDaoExt;
import com.wisdom.auth.service.SysRoleServiceExt;
import com.wisdom.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统角色表 逻辑层
 *
 * @author captain
 * @version 1.0
 */
@Slf4j
@Service
public class SysRoleServiceImplExt extends BaseServiceImpl<SysRoleDaoExt, SysRoleExt> implements SysRoleServiceExt {
    @Autowired
    private SysRoleDaoExt sysRoleDaoExt;

}
