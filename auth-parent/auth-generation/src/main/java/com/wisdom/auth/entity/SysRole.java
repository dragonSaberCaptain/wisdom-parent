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
 * 系统角色表 实体类
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2021-09-15 13:45:02 星期三
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "SYS_ROLE")
@Table(appliesTo = "SYS_ROLE", comment = "系统角色表")
@ApiModel(value = "SysRole对象", description = "系统角色表")
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "描述")
    @Column(columnDefinition = "varchar(255) COMMENT '描述'")
    private String description;

    @ApiModelProperty(value = "角色中文名字")
    @Column(columnDefinition = "varchar(64) COMMENT '角色中文名字'")
    private String nameCn;

    @ApiModelProperty(value = "角色英文名字")
    @Column(columnDefinition = "varchar(64) COMMENT '角色英文名字'")
    private String nameEn;

    @ApiModelProperty(value = "父类ID")
    @Column(columnDefinition = "varchar(32) COMMENT '父类ID'")
    private String parentId;


}
