package com.wisdom.auth.service;

import com.wisdom.auth.entity.SysUser;
import com.wisdom.common.service.BaseService;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Copyright ©2022 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 系统用户表 业务层
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2022-01-13 13:00:31 星期四
 */
public interface SysUserServiceExt extends BaseService<SysUser>, UserDetailsService {

}
