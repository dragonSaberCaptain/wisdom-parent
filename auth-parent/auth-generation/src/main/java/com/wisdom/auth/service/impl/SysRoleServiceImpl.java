package com.wisdom.auth.service.impl;

import com.wisdom.auth.entity.SysRole;
import com.wisdom.auth.dao.SysRoleDao;
import com.wisdom.auth.service.SysRoleService;
import com.wisdom.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 系统角色表 逻辑层
 *
 * @author captain
 * @version 1.0.0
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleDao, SysRole> implements SysRoleService {

}
