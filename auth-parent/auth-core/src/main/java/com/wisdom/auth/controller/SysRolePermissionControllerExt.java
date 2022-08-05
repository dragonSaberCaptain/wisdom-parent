package com.wisdom.auth.controller;

import com.wisdom.auth.service.SysRolePermissionServiceExt;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统角色权限表 控制层
 *
 * @author captain
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/auth/sysRolePermission")
@Api(value = "SysRolePermissionController API", tags = "sys_role_permission:系统角色权限表 相关接口")
public class SysRolePermissionControllerExt {
    @Autowired
    private SysRolePermissionServiceExt sysRolePermissionServiceExt;

}

