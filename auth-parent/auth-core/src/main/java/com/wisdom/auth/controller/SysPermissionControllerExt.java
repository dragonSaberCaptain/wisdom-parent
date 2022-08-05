package com.wisdom.auth.controller;

import com.wisdom.auth.service.SysPermissionServiceExt;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统权限表 控制层
 *
 * @author captain
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/auth/sysPermission")
@Api(value = "SysPermissionController API", tags = "sys_permission:系统权限表 相关接口")
public class SysPermissionControllerExt {
    @Autowired
    private SysPermissionServiceExt sysPermissionServiceExt;

}

