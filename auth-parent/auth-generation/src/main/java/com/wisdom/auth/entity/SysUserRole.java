package com.wisdom.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.wisdom.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
/**
 * Copyright ©2021 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 系统用户角色表 实体类
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2021-09-15 13:45:02 星期三
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "SYS_USER_ROLE")
@Table(appliesTo = "SYS_USER_ROLE", comment = "系统用户角色表")
@ApiModel(value = "SysUserRole对象", description = "系统用户角色表")
public class SysUserRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色id")
    @Column(columnDefinition = "varchar(32) COMMENT '角色id'")
    private String roleId;

    @ApiModelProperty(value = "用户id")
    @Column(columnDefinition = "varchar(32) COMMENT '用户id'")
    private String userId;


}
