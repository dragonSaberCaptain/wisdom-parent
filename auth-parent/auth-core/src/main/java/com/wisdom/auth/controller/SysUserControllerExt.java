package com.wisdom.auth.controller;

import com.wisdom.auth.service.SysUserServiceExt;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统用户表 控制层
 *
 * @author captain
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/auth/sysUser")
@Api(value = "SysUserController API", tags = "sys_user:系统用户表 相关接口")
public class SysUserControllerExt {
    @Autowired
    private SysUserServiceExt sysUserServiceExt;

    @GetMapping("/test")
    public void test(){}

}

