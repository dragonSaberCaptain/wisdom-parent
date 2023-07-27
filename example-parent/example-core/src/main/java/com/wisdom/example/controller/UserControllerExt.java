package com.wisdom.example.controller;

import com.wisdom.example.service.UserServiceExt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 用户表 控制层
 *
 * @author captain
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/example/user")
public class UserControllerExt {
    @Autowired
    private UserServiceExt userServiceExt;

}

