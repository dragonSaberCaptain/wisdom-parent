package com.wisdom.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * Copyright ©2022 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 测试表 实体类
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2022-01-04 13:44:06 星期二
 */
//lombok
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
//jpa
@Entity(name = "TEST")
@Table(appliesTo = "TEST", comment = "测试表")
//swagger
@ApiModel(value = "Test对象", description = "测试表")
public class Test extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "测试列")
    @Column(columnDefinition = "varchar(64) COMMENT '测试列'")
    private String testColumn;


}
