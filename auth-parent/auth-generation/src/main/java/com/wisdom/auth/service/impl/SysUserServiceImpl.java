package com.wisdom.auth.service.impl;

import com.wisdom.auth.entity.SysUser;
import com.wisdom.auth.dao.SysUserDao;
import com.wisdom.auth.service.SysUserService;
import com.wisdom.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 系统用户表 逻辑层
 *
 * @author captain
 * @version 1.0
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserDao, SysUser> implements SysUserService {

}
