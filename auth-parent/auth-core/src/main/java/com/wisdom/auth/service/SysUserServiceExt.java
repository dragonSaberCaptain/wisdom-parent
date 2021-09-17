package com.wisdom.auth.service;

import com.wisdom.auth.entity.SysUser;
import com.wisdom.common.service.BaseService;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Copyright ©2021 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 业务层
 * </p>
 *
 * @author captain
 * @version 1.0
 * @createDate 2021-09-13 15:34:22 星期一
 */
public interface SysUserServiceExt extends BaseService<SysUser>, UserDetailsService {

}
