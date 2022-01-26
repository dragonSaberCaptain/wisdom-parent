package com.wisdom.example.service.impl;

import com.wisdom.example.entity.User;
import com.wisdom.example.dao.UserDao;
import com.wisdom.example.service.UserService;
import com.wisdom.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户表 逻辑层
 *
 * @author captain
 * @version 1.0
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserDao, User> implements UserService {

}
