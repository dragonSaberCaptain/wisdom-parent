package com.wisdom.auth.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * 系统权限表 实体类 扩展
 *
 * @author captain
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysPermissionExt对象 扩展", description = "系统权限表")
public class SysPermissionExt extends SysPermission {

}
