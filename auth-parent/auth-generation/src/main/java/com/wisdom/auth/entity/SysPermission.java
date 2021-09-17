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
 * 系统权限表 实体类
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2021-09-15 13:45:02 星期三
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "SYS_PERMISSION")
@Table(appliesTo = "SYS_PERMISSION", comment = "系统权限表")
@ApiModel(value = "SysPermission对象", description = "系统权限表")
public class SysPermission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "描述")
    @Column(columnDefinition = "varchar(255) COMMENT '描述'")
    private String description;

    @ApiModelProperty(value = "权限中文名字")
    @Column(columnDefinition = "varchar(64) COMMENT '权限中文名字'")
    private String nameCn;

    @ApiModelProperty(value = "权限英文名字")
    @Column(columnDefinition = "varchar(64) COMMENT '权限英文名字'")
    private String nameEn;

    @ApiModelProperty(value = "父类ID")
    @Column(columnDefinition = "varchar(32) COMMENT '父类ID'")
    private String parentId;

    @ApiModelProperty(value = "权限请求路径")
    @Column(columnDefinition = "varchar(255) COMMENT '权限请求路径'")
    private String url;


}
