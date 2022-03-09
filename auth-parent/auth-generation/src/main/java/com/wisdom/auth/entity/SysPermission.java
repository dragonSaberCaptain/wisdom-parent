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
import javax.persistence.Id;
/**
 * 系统权限表 实体类
 *
 * @author captain
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Entity(name = "SYS_PERMISSION")
@Table(appliesTo = "SYS_PERMISSION", comment = "系统权限表")
@ApiModel(value = "SysPermission对象", description = "系统权限表")
public class SysPermission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "描述")
    @Column(columnDefinition = "varchar(255) COMMENT '描述'")
    protected String description;

    @ApiModelProperty(value = "权限中文名字")
    @Column(columnDefinition = "varchar(64) COMMENT '权限中文名字'")
    protected String nameCn;

    @ApiModelProperty(value = "权限英文名字")
    @Column(columnDefinition = "varchar(64) COMMENT '权限英文名字'")
    protected String nameEn;

    @ApiModelProperty(value = "父类ID")
    @Column(columnDefinition = "varchar(32) COMMENT '父类ID'")
    protected String parentId;

    @ApiModelProperty(value = "权限请求路径")
    @Column(columnDefinition = "varchar(255) COMMENT '权限请求路径'")
    protected String url;
}
