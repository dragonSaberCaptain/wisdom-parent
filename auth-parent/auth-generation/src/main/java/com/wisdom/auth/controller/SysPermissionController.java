package com.wisdom.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisdom.auth.service.SysPermissionService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import com.wisdom.common.controller.BaseController;
import com.wisdom.auth.entity.SysPermission;
/**
 * 系统权限表 控制层
 *
 * @author captain
 * @version 1.0.0
 */
@RestController
@RequestMapping("/auth/sysPermission")
@Api(value = "SysPermissionController API", tags = "sys_permission:系统权限表 相关接口")
public class SysPermissionController extends BaseController<SysPermissionService, SysPermission> {

}

