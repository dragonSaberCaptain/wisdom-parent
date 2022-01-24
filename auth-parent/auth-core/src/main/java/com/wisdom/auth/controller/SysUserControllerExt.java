package com.wisdom.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisdom.auth.service.SysUserServiceExt;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import com.wisdom.common.controller.BaseController;
import com.wisdom.auth.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
/**
 * 系统用户表 控制层
 *
 * @author captain
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/auth/sysUser")
@Api(value = "SysUserControllerExt API", tags = "sys_user:系统用户表 相关扩展接口")
public class SysUserControllerExt extends BaseController<SysUserServiceExt, SysUser> {
    @Autowired
    private SysUserServiceExt sysUserServiceExt;

}

