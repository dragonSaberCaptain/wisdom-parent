package com.wisdom.auth.service.impl;

import com.wisdom.auth.entity.SysUserExt;
import com.wisdom.auth.dao.SysUserDaoExt;
import com.wisdom.auth.service.SysUserServiceExt;
import com.wisdom.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统用户表 逻辑层
 *
 * @author captain
 * @version 1.0
 */
@Slf4j
@Service
public class SysUserServiceImplExt extends BaseServiceImpl<SysUserDaoExt, SysUserExt> implements SysUserServiceExt {
    @Autowired
    private SysUserDaoExt sysUserDaoExt;

}
