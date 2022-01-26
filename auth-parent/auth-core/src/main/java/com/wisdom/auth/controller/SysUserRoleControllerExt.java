package com.wisdom.auth.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisdom.auth.service.SysUserRoleServiceExt;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
/**
 * 系统用户角色表 控制层
 *
 * @author captain
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/auth/sysUserRole")
@Api(value = "SysUserRoleController API", tags = "sys_user_role:系统用户角色表 相关接口")
public class SysUserRoleControllerExt {
    @Autowired
    private SysUserRoleServiceExt sysUserRoleServiceExt;

}

