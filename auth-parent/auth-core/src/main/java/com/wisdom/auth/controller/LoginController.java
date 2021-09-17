package com.wisdom.auth.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * TODO (用一句话描述该类的作用)
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/14 10:00 星期二
 */
@Slf4j
@Controller
@RequestMapping("/auth")
@Api(value = "LoginController API", tags = "系统登录相关接口")
public class LoginController {
    @GetMapping("page")
    public String loginPage() {
        return "auth-page";
    }
}
