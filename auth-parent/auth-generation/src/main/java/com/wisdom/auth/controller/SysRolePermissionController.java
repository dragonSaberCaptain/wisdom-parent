package com.wisdom.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisdom.auth.service.SysRolePermissionService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import com.wisdom.common.controller.BaseController;
import com.wisdom.auth.entity.SysRolePermission;
/**
 * 系统角色权限表 控制层
 *
 * @author captain
 * @version 1.0.0
 */
@RestController
@RequestMapping("/auth/sysRolePermission")
@Api(value = "SysRolePermissionController API", tags = "sys_role_permission:系统角色权限表 相关接口")
public class SysRolePermissionController extends BaseController<SysRolePermissionService, SysRolePermission> {

}

