package com.wisdom.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisdom.auth.service.SysPermissionServiceExt;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import com.wisdom.common.controller.BaseController;
import com.wisdom.auth.entity.SysPermission;
import lombok.extern.slf4j.Slf4j;
/**
 * 系统权限表 控制层
 *
 * @author captain
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/auth/sysPermission")
@Api(value = "SysPermissionControllerExt API", tags = "sys_permission:系统权限表 相关扩展接口")
public class SysPermissionControllerExt extends BaseController<SysPermissionServiceExt, SysPermission> {
    @Autowired
    private SysPermissionServiceExt sysPermissionServiceExt;

}

