package com.wisdom.example.service.impl;

import com.wisdom.example.entity.User;
import com.wisdom.example.dao.UserDao;
import com.wisdom.example.service.UserService;
import com.wisdom.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

/**
 * Copyright ©2022 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 用户表 逻辑层
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2022-01-04 13:44:06 星期二
 */
@Slf4j
@Service
public class UserServiceImpl extends BaseServiceImpl<UserDao, User> implements UserService {
    @Autowired
    private UserDao userDao;

}
