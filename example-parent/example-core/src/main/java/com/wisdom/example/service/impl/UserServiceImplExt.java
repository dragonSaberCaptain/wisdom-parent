package com.wisdom.example.service.impl;

import com.wisdom.common.service.impl.BaseServiceImpl;
import com.wisdom.example.dao.UserDaoExt;
import com.wisdom.example.entity.UserExt;
import com.wisdom.example.service.UserServiceExt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户表 逻辑层
 *
 * @author captain
 * @version 1.0
 */
@Slf4j
@Service
public class UserServiceImplExt extends BaseServiceImpl<UserDaoExt, UserExt> implements UserServiceExt {
    @Autowired
    private UserDaoExt userDaoExt;

}
