package com.wisdom.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 系统字典表 实体类
 *
 * @author captain
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Entity(name = "SYS_DICT")
@Table(appliesTo = "SYS_DICT", comment = "系统字典表")
public class SystemDictEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "varchar(64) COMMENT '字典类型'")
    protected String dictType;

    @Column(columnDefinition = "varchar(64) COMMENT '字典类型描述'")
    protected String dictTypeDesc;

    @Column(columnDefinition = "varchar(64) COMMENT '字典值'")
    protected String dictValue;

    @Column(columnDefinition = "varchar(64) COMMENT '字典值描述'")
    protected String dictValueDesc;

    @Column(columnDefinition = "varchar(64) COMMENT '描述语言'")
    protected String locale;
}
