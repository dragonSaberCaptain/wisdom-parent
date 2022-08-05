package com.wisdom.example.controller;

import com.wisdom.example.service.UserServiceExt;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户表 控制层
 *
 * @author captain
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/example/user")
@Api(value = "UserController API", tags = "user:用户表 相关接口")
public class UserControllerExt {
    @Autowired
    private UserServiceExt userServiceExt;

}

