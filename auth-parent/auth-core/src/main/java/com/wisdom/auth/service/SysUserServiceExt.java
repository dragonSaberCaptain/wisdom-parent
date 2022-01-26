package com.wisdom.auth.service;

import com.wisdom.auth.entity.SysUserExt;
import com.wisdom.common.service.BaseService;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 系统用户表 业务层
 *
 * @author captain
 * @version 1.0
 */
public interface SysUserServiceExt extends BaseService<SysUserExt>, UserDetailsService {

}
