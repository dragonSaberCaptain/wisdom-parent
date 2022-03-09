package com.wisdom.auth.service.impl;

import com.wisdom.auth.entity.SysUserRole;
import com.wisdom.auth.dao.SysUserRoleDao;
import com.wisdom.auth.service.SysUserRoleService;
import com.wisdom.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 系统用户角色表 逻辑层
 *
 * @author captain
 * @version 1.0.0
 */
@Service
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRoleDao, SysUserRole> implements SysUserRoleService {

}
