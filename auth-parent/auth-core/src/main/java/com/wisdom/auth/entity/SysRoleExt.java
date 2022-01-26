package com.wisdom.auth.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * 系统角色表 实体类 扩展
 *
 * @author captain
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysRoleExt对象 扩展", description = "系统角色表")
public class SysRoleExt extends SysRole {

}
