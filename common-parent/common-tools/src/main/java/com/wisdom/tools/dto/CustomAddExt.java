package com.wisdom.tools.dto;

import com.wisdom.tools.datetime.DateUtilByZoned;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;

/**
 * Copyright © 2022 dragonSaberCaptain. All rights reserved.
 * <p>
 * 自定义附加字段 主要用于mybatisplus中velocity模板使用
 *
 * @author captain
 * @version 1.0
 * @datetime 2022/2/8 14:59 星期二
 */
@Data
@Accessors(chain = true)
public class CustomAddExt {
    /**
     * 版本号
     */
    protected String version;
    /**
     * 创建时间
     */
    protected String createDate;
    /**
     * 年份
     */
    protected Integer year;
    /**
     * 扩展实体继承的父类
     */
    protected String superEntityClassPackage;
    /**
     * 是否开启jpa相关注解
     */
    protected Boolean useJpa;
    /**
     * 是否开启扩展生成
     */
    protected Boolean openExt;

    /**
     * 扩展 是否覆盖文件
     */
    protected Boolean fileOverride = false;

    public CustomAddExt() {
        ZonedDateTime zonedDateTime = DateUtilByZoned.now();
        this.createDate = DateUtilByZoned.getDateTime() + " " + DateUtilByZoned.getDayOfWeekCn(zonedDateTime);
        this.year = zonedDateTime.getYear();
    }
}
