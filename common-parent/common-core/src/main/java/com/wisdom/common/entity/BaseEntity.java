package com.wisdom.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Copyright © 2018 dragonSaberCaptain. All rights reserved.
 * <p>
 * //@JsonSerialize(using = ToStringSerializer.class)  //将结果转成String
 *
 * @author captain
 * @version 1.0
 * @datetime 2018/05/10 10:31 星期三
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseEntity implements Serializable {
    @Id
    @TableId(type = IdType.ASSIGN_ID)  //雪花算法
    @Column(updatable = false, columnDefinition = "varchar(64) COMMENT '主键id'")
    @ApiModelProperty(value = "主键id")
    protected String id;

    @Column(updatable = false, columnDefinition = "varchar(64) COMMENT '创建日期时间'")
    @ApiModelProperty(value = "创建日期时间")
    protected String createDateTime;

    @Column(updatable = false, columnDefinition = "varchar(20) COMMENT '创建者'")
    @ApiModelProperty(value = "创建者")
    protected String createId;

    @Column(columnDefinition = "varchar(64) COMMENT '更新日期时间'")
    @ApiModelProperty(value = "更新日期时间")
    protected String upDateTime;

    @Column(columnDefinition = "varchar(20) COMMENT '更新者'")
    @ApiModelProperty(value = "更新者")
    protected String updateId;

    @Column(columnDefinition = "varchar(255) COMMENT '备注'")
    @ApiModelProperty(value = "备注")
    protected String remark;

    @Version
    @Column(columnDefinition = "integer default 1 COMMENT '版本号'")
    @ApiModelProperty(value = "版本号")
    protected Integer version;

    @TableLogic
    @Column(columnDefinition = "integer default 0 COMMENT '是否隐藏：0否 1是  默认：0'")
    @ApiModelProperty(value = "是否隐藏")
    protected Integer delFlag;

    @Transient
    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty(value = "创建日期时间查询开始时间(查询时间范围)")
    protected String createDateTimeStart;

    @Transient
    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty(value = "创建日期时间查询结束时间(查询时间范围)")
    protected String createDateTimeEnd;

    @Transient
    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty(value = "更新日期时间查询开始时间(查询时间范围)")
    protected String upDateTimeStart;

    @Transient
    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty(value = "更新日期时间查询结束时间(查询时间范围)")
    protected String upDateTimeEnd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getUpDateTime() {
        return upDateTime;
    }

    public void setUpDateTime(String upDateTime) {
        this.upDateTime = upDateTime;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getCreateDateTimeStart() {
        return createDateTimeStart;
    }

    public void setCreateDateTimeStart(String createDateTimeStart) {
        this.createDateTimeStart = createDateTimeStart;
    }

    public String getCreateDateTimeEnd() {
        return createDateTimeEnd;
    }

    public void setCreateDateTimeEnd(String createDateTimeEnd) {
        this.createDateTimeEnd = createDateTimeEnd;
    }

    public String getUpDateTimeStart() {
        return upDateTimeStart;
    }

    public void setUpDateTimeStart(String upDateTimeStart) {
        this.upDateTimeStart = upDateTimeStart;
    }

    public String getUpDateTimeEnd() {
        return upDateTimeEnd;
    }

    public void setUpDateTimeEnd(String upDateTimeEnd) {
        this.upDateTimeEnd = upDateTimeEnd;
    }
}
