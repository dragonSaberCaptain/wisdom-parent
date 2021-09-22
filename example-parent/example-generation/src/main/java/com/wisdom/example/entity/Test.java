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
 * 测试表 实体类
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2021-09-15 13:44:52 星期三
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "TEST")
@Table(appliesTo = "TEST", comment = "测试表")
@ApiModel(value = "Test对象", description = "测试表")
public class Test extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "测试列")
    @Column(columnDefinition = "varchar(64) COMMENT '测试列'")
    private String testColumn;


}
