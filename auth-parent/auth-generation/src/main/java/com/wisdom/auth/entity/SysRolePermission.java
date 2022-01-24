package com.wisdom.auth.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.wisdom.common.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
/**
 * 系统角色权限表 实体类
 *
 * @author captain
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Entity(name = "SYS_ROLE_PERMISSION")
@Table(appliesTo = "SYS_ROLE_PERMISSION", comment = "系统角色权限表")
@ApiModel(value = "SysRolePermission对象", description = "系统角色权限表")
public class SysRolePermission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限id")
    @Column(columnDefinition = "varchar(32) COMMENT '权限id'")
    private String permissionId;

    @ApiModelProperty(value = "角色id")
    @Column(columnDefinition = "varchar(32) COMMENT '角色id'")
    private String roleId;
}
