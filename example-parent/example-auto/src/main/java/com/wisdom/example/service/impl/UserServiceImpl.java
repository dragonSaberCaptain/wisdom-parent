package com.wisdom.example.service.impl;

import com.wisdom.core.service.impl.BaseServiceImpl;
import com.wisdom.example.dao.UserDao;
import com.wisdom.example.entity.User;
import com.wisdom.example.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户表 逻辑层
 *
 * @author captain
 * @version 1.0.0
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserDao, User> implements UserService {

}
