package com.wisdom.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisdom.auth.service.SysUserRoleService;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import com.wisdom.common.controller.BaseController;
import com.wisdom.auth.entity.SysUserRole;
import lombok.extern.slf4j.Slf4j;

/**
 * Copyright ©2021 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 系统用户角色表 控制层
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2021-09-15 13:45:02 星期三
 */
@Slf4j
@Controller
@RequestMapping("/auth/sysUserRole")
@Api(value = "SysUserRoleController API", tags = "sys_user_role:系统用户角色表相关接口")
public class SysUserRoleController extends BaseController<SysUserRoleService, SysUserRole> {
    @Autowired
    private SysUserRoleService sysUserRoleService;

}

