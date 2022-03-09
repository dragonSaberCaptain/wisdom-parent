package com.wisdom.auth.service.impl;

import com.wisdom.auth.entity.SysPermission;
import com.wisdom.auth.dao.SysPermissionDao;
import com.wisdom.auth.service.SysPermissionService;
import com.wisdom.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 系统权限表 逻辑层
 *
 * @author captain
 * @version 1.0.0
 */
@Service
public class SysPermissionServiceImpl extends BaseServiceImpl<SysPermissionDao, SysPermission> implements SysPermissionService {

}
