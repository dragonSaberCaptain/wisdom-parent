package com.wisdom.auth.service.impl;

import com.wisdom.auth.entity.UserExt;
import com.wisdom.auth.dao.UserDaoExt;
import com.wisdom.auth.service.UserServiceExt;
import com.wisdom.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户表 逻辑层
 *
 * @author captain
 * @version 1.0.0
 */
@Slf4j
@Service
public class UserServiceImplExt extends BaseServiceImpl<UserDaoExt, UserExt> implements UserServiceExt {
    @Autowired
    private UserDaoExt userDaoExt;

}
