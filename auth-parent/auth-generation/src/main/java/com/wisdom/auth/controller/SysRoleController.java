package com.wisdom.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisdom.auth.service.SysRoleService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import com.wisdom.common.controller.BaseController;
import com.wisdom.auth.entity.SysRole;
/**
 * 系统角色表 控制层
 *
 * @author captain
 * @version 1.0
 */
@RestController
@RequestMapping("/auth/sysRole")
@Api(value = "SysRoleController API", tags = "sys_role:系统角色表 相关接口")
public class SysRoleController extends BaseController<SysRoleService, SysRole> {

}

