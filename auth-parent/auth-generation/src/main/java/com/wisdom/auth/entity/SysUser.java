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
 * 系统用户表 实体类
 *
 * @author captain
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Entity(name = "SYS_USER")
@Table(appliesTo = "SYS_USER", comment = "系统用户表")
@ApiModel(value = "SysUser对象", description = "系统用户表")
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "账号")
    @Column(columnDefinition = "varchar(64) COMMENT '账号'")
    protected String account;

    @ApiModelProperty(value = "密码")
    @Column(columnDefinition = "varchar(255) COMMENT '密码'")
    protected String password;

    @ApiModelProperty(value = "登录状态：0否 1是  默认：0")
    @Column(columnDefinition = "varchar(2) COMMENT '登录状态：0否 1是  默认：0'")
    protected String loginFlag;
}
