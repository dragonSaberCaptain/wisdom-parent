package com.wisdom.auth.controller;

import com.wisdom.auth.entity.SysRole;
import com.wisdom.auth.service.SysRoleService;
import com.wisdom.common.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Copyright ©2021 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 系统角色表 控制层
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2021-09-15 13:45:02 星期三
 */
@Slf4j
@Controller
@RequestMapping("/auth/sysRole")
@Api(value = "SysRoleController API", tags = "sys_role:系统角色表相关接口")
public class SysRoleController extends BaseController<SysRoleService, SysRole> {
    @Autowired
    private SysRoleService sysRoleService;

}

