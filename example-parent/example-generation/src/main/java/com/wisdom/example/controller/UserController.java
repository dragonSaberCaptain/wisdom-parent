package com.wisdom.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisdom.example.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import com.wisdom.common.controller.BaseController;
import com.wisdom.example.entity.User;
/**
 * 用户表 控制层
 *
 * @author captain
 * @version 1.0
 */
@RestController
@RequestMapping("/example/user")
@Api(value = "UserController API", tags = "user:用户表 相关接口")
public class UserController extends BaseController<UserService, User> {

}

