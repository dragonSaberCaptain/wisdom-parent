package com.wisdom.auth.service.impl;

import com.wisdom.auth.entity.User;
import com.wisdom.auth.dao.UserDao;
import com.wisdom.auth.service.UserService;
import com.wisdom.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户表 逻辑层
 *
 * @author captain
 * @version 1.0.0
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserDao, User> implements UserService {

}
