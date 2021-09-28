package com.wisdom.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisdom.auth.service.SysUserService;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import com.wisdom.common.controller.BaseController;
import com.wisdom.auth.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
/**
 * Copyright ©2021 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 系统用户表 控制层
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2021-09-28 13:08:29 星期二
 */
@Slf4j
@Controller
@RequestMapping("/auth/sysUser")
@Api(value = "SysUserController API", tags = "sys_user:系统用户表相关接口")
public class SysUserController extends BaseController<SysUserService, SysUser> {
    @Autowired
    private SysUserService sysUserService;

}

