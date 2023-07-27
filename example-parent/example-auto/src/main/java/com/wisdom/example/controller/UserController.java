package com.wisdom.example.controller;

import com.wisdom.core.controller.BaseController;
import com.wisdom.example.entity.User;
import com.wisdom.example.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户表 控制层
 *
 * @author captain
 * @version 1.0.0
 */
@RestController
@RequestMapping("/example/user")
public class UserController extends BaseController<UserService, User> {

}

