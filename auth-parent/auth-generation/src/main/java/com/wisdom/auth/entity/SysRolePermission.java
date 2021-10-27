package com.wisdom.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.wisdom.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
/**
 * Copyright ©2021 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 系统角色权限表 实体类
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2021-10-27 17:12:58 星期三
 */
@Data
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
