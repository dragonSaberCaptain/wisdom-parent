package com.wisdom.auth.service.impl;

import com.wisdom.auth.dao.SysPermissionDaoExt;
import com.wisdom.auth.dao.SysUserDao;
import com.wisdom.auth.dao.SysUserDaoExt;
import com.wisdom.auth.entity.SysPermission;
import com.wisdom.auth.entity.SysPermissionExt;
import com.wisdom.auth.entity.SysUser;
import com.wisdom.auth.entity.SysUserExt;
import com.wisdom.auth.service.SysUserServiceExt;
import com.wisdom.common.service.impl.BaseServiceImpl;
import com.wisdom.common.tools.mybatisplus.MybatisplusUtil;
import com.wisdom.config.enums.HttpEnum;
import com.wisdom.config.exception.ResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    private SysUserDao sysUserDao;

    @Autowired
    private SysPermissionDaoExt sysPermissionDaoExt;

    /**
     * 查询数据库用户信息
     *
     * @param username 登录账户
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = new SysUser();
        sysUser.setAccount(username);
        sysUser = sysUserDao.selectOne(MybatisplusUtil.createWrapper(sysUser));
        //验证账户为username的用户是否存在
        if (null == sysUser) {
            log.info("登录失败,未找到用户:" + username);
            throw new ResultException(HttpEnum.NO_CONTENT);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        //获取用户权限
        List<SysPermission> sysPermissionExts = sysPermissionDaoExt.selectByUserId(sysUser.getId());
        //设置用户权限
        sysPermissionExts.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getNameEn())));

        //返回认证用户
        return new User(sysUser.getAccount(), sysUser.getPassword(), authorities);
    }
}
