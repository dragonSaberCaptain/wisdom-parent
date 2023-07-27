package com.wisdom.example.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.wisdom.core.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 用户表 实体类
 *
 * @author captain
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Entity(name = "USER")
@Table(appliesTo = "USER", comment = "用户表")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Id
    @Column(columnDefinition = "varchar(64) COMMENT '主键id'")
    protected String id;

    /**
     * 创建日期时间
     */
    @Column(columnDefinition = "varchar(64) COMMENT '创建日期时间'")
    protected String createDateTime;

    /**
     * 创建者
     */
    @Column(columnDefinition = "varchar(20) COMMENT '创建者'")
    protected String createId;

    @TableLogic
    /**
     * 是否隐藏：0否 1是  默认：0
     */
    @Column(columnDefinition = "int COMMENT '是否隐藏：0否 1是  默认：0'")
    protected Integer delFlag;

    /**
     * 备注
     */
    @Column(columnDefinition = "varchar(255) COMMENT '备注'")
    protected String remark;

    /**
     * 更新日期时间
     */
    @Column(columnDefinition = "varchar(64) COMMENT '更新日期时间'")
    protected String upDateTime;

    /**
     * 更新者
     */
    @Column(columnDefinition = "varchar(20) COMMENT '更新者'")
    protected String updateId;

    @Version
    /**
     * 版本号
     */
    @Column(columnDefinition = "int COMMENT '版本号'")
    protected Integer version;

    /**
     * 账号
     */
    @Column(columnDefinition = "varchar(64) COMMENT '账号'")
    protected String account;

    /**
     * 密码
     */
    @Column(columnDefinition = "varchar(64) COMMENT '密码'")
    protected String password;
}
