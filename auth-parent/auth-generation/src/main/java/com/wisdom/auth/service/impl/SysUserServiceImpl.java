package com.wisdom.auth.service.impl;

import com.wisdom.auth.dao.SysUserDao;
import com.wisdom.auth.entity.SysUser;
import com.wisdom.auth.service.SysUserService;
import com.wisdom.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Copyright ©2021 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 系统用户表 逻辑层
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2021-09-15 13:45:02 星期三
 */
@Slf4j
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserDao, SysUser> implements SysUserService {
    @Autowired
    private SysUserDao sysUserDao;

}
