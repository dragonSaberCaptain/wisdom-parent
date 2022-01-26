package com.wisdom.example.entity;

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
 * 用户表 实体类
 *
 * @author captain
 * @version 1.0
 */
@Data
@Accessors(chain = true)
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
