package com.wisdom.example.entity;

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
 * 用户表 实体类
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2021-09-15 13:44:52 星期三
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "USER")
@Table(appliesTo = "USER", comment = "用户表")
@ApiModel(value = "User对象", description = "用户表")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "账号")
    @Column(columnDefinition = "varchar(64) COMMENT '账号'")
    private String account;

    @ApiModelProperty(value = "密码")
    @Column(columnDefinition = "varchar(64) COMMENT '密码'")
    private String password;


}
