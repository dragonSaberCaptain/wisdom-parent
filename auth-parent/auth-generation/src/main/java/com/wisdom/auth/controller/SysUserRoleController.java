package com.wisdom.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisdom.auth.service.SysUserRoleService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import com.wisdom.common.controller.BaseController;
import com.wisdom.auth.entity.SysUserRole;
/**
 * 系统用户角色表 控制层
 *
 * @author captain
 * @version 1.0.0
 */
@RestController
@RequestMapping("/auth/sysUserRole")
@Api(value = "SysUserRoleController API", tags = "sys_user_role:系统用户角色表 相关接口")
public class SysUserRoleController extends BaseController<SysUserRoleService, SysUserRole> {

}

