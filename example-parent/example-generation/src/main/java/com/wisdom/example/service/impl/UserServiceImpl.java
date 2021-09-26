package com.wisdom.example.service.impl;

import com.wisdom.common.service.impl.BaseServiceImpl;
import com.wisdom.example.dao.UserDao;
import com.wisdom.example.entity.User;
import com.wisdom.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Copyright ©2021 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 用户表 逻辑层
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2021-09-23 09:43:03 星期四
 */
@Slf4j
@Service
public class UserServiceImpl extends BaseServiceImpl<UserDao, User> implements UserService {
    @Autowired
    private UserDao userDao;

}
