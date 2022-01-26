package com.wisdom.auth.service.impl;

import com.wisdom.auth.entity.SysPermissionExt;
import com.wisdom.auth.dao.SysPermissionDaoExt;
import com.wisdom.auth.service.SysPermissionServiceExt;
import com.wisdom.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统权限表 逻辑层
 *
 * @author captain
 * @version 1.0
 */
@Slf4j
@Service
public class SysPermissionServiceImplExt extends BaseServiceImpl<SysPermissionDaoExt, SysPermissionExt> implements SysPermissionServiceExt {
    @Autowired
    private SysPermissionDaoExt sysPermissionDaoExt;

}
