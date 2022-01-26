package com.wisdom.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisdom.example.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import com.wisdom.common.controller.BaseController;
import com.wisdom.example.entity.User;
import lombok.extern.slf4j.Slf4j;
/**
 * Copyright ©2022 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 用户表 控制层
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2022-01-04 13:44:06 星期二
 */
@Slf4j
@RestController
@RequestMapping("/example/user")
@Api(value = "UserController API", tags = "user:用户表相关接口")
public class UserController extends BaseController<UserService, User> {
    @Autowired
    private UserService userService;

}

