package com.wisdom.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import com.wisdom.auth.service.SysRolePermissionService;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import com.wisdom.common.controller.BaseController;
import com.wisdom.auth.entity.SysRolePermission;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * Copyright ©2021 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 系统角色权限表 控制层
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2021-09-15 13:45:02 星期三
 */
@Slf4j
@Controller
@RequestMapping("/auth/sysRolePermission")
@Api(value = "SysRolePermissionController API", tags = "sys_role_permission:系统角色权限表相关接口")
public class SysRolePermissionController extends BaseController<SysRolePermissionService, SysRolePermission> {
    @Resource
    private SysRolePermissionService sysRolePermissionService;

}

