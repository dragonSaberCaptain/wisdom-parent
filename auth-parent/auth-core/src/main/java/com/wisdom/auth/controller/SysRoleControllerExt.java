package com.wisdom.auth.controller;

import com.wisdom.auth.service.SysRoleServiceExt;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统角色表 控制层
 *
 * @author captain
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/auth/sysRole")
@Api(value = "SysRoleController API", tags = "sys_role:系统角色表 相关接口")
public class SysRoleControllerExt {
    @Autowired
    private SysRoleServiceExt sysRoleServiceExt;

}

