package com.wisdom.auth.service.impl;

import com.wisdom.auth.dao.SysPermissionDaoExt;
import com.wisdom.auth.dao.SysUserDao;
import com.wisdom.auth.entity.SysPermission;
import com.wisdom.auth.entity.SysUser;
import com.wisdom.auth.dao.SysUserDaoExt;
import com.wisdom.auth.service.SysUserServiceExt;
import com.wisdom.common.service.impl.BaseServiceImpl;
import com.wisdom.common.tools.mybatisplus.MybatisplusUtil;
import com.wisdom.config.enums.ResultEnum;
import com.wisdom.config.exception.ResultException;
import com.wisdom.tools.string.StringUtil;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright ©2022 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 系统用户表 逻辑层
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2022-01-13 13:00:31 星期四
 */
@Slf4j
@Service
public class SysUserServiceImplExt extends BaseServiceImpl<SysUserDaoExt, SysUser> implements SysUserServiceExt {
    @Autowired
    private SysUserDaoExt sysUserDaoExt;

    @Autowired
    private SysPermissionDaoExt sysPermissionDaoExt;

    /**
     * 查询数据库用户信息
     *
     * @param username 登录账户
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtil.isBlank(username)) {
            throw new ResultException(ResultEnum.PARAMS_ERROR);
        }
        SysUser sysUser = new SysUser();
        sysUser.setAccount(username);
        sysUser = sysUserDaoExt.selectOne(MybatisplusUtil.createWrapper(sysUser));
        //验证账户为username的用户是否存在
        if (null == sysUser) {
            log.info("登录失败,未找到用户:" + username);
            throw new ResultException(ResultEnum.DATA_NOT_FOUND);
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        //获取用户权限
        List<SysPermission> permissions = sysPermissionDaoExt.selectByUserId(sysUser.getId());
        //设置用户权限
        permissions.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getNameEn())));

        //返回认证用户
        return new User(sysUser.getAccount(), sysUser.getPassword(), authorities);
    }
}
