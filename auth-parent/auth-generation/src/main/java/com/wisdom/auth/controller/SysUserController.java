package com.wisdom.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisdom.auth.service.SysUserService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import com.wisdom.common.controller.BaseController;
import com.wisdom.auth.entity.SysUser;
/**
 * 系统用户表 控制层
 *
 * @author captain
 * @version 1.0.0
 */
@RestController
@RequestMapping("/auth/sysUser")
@Api(value = "SysUserController API", tags = "sys_user:系统用户表 相关接口")
public class SysUserController extends BaseController<SysUserService, SysUser> {

}

