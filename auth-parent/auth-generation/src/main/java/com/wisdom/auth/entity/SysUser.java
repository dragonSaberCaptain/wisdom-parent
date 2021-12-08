package com.wisdom.auth.entity;

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
 * 系统用户表 实体类
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2021-10-27 17:12:58 星期三
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "SYS_USER")
@Table(appliesTo = "SYS_USER", comment = "系统用户表")
@ApiModel(value = "SysUser对象", description = "系统用户表")
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "账号")
    @Column(columnDefinition = "varchar(64) COMMENT '账号'")
    private String account;

    @ApiModelProperty(value = "密码")
    @Column(columnDefinition = "varchar(255) COMMENT '密码'")
    private String password;

    @ApiModelProperty(value = "登录状态")
    @Column(columnDefinition = "varchar(2) default 0 COMMENT '登录状态：0否 1是  默认：0'")
    private String loginFlag;
}
