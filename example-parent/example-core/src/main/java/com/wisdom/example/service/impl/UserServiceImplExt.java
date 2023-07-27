package com.wisdom.example.service.impl;

import com.wisdom.example.entity.UserExt;
import com.wisdom.example.dao.UserDaoExt;
import com.wisdom.example.service.UserServiceExt;
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
