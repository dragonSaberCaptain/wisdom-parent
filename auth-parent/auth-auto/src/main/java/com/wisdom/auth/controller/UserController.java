package com.wisdom.auth.controller;

import com.wisdom.auth.entity.User;
import com.wisdom.auth.service.UserService;
import com.wisdom.core.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户表 控制层
 *
 * @author captain
 * @version 1.0.0
 */
@RestController
@RequestMapping("/auth/user")
public class UserController extends BaseController<UserService, User> {

}

