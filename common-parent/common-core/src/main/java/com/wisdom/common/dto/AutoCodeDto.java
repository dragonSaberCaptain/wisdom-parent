package com.wisdom.common.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * TODO (用一句话描述该类的作用)
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/10/27 10:34 星期三
 */
@Data
@Slf4j
public class AutoCodeDto {
    private String driverName;
    private String urlDb;
    private String username;
    private String password;
    /**
     * 生成的模块的父路径：如 com.wisdom
     */
    private String moduleParentPath;
    /**
     * 生成的模块名称
     */
    private String moduleName;
    /**
     * 生成文件的输出目录
     */
    private String outputDir;

    /**
     * 要去除的表前缀： 如定义sys 则sys_user 生成后为:user
     */
    private String tablePrefix;

    /**
     * 要去除的字段前缀： 如定义t 则t_id 生成后为:id
     */
    private String fieldPrefix;

    /**
     * 需要包含的表名，允许正则表达式（与exclude二选一配置）
     */
    private String include;

    /**
     * 需要包含的表名，允许正则表达式（与include二选一配置）
     */
    private String exclude;

    /**
     * 自定义参数配置
     */
    private Map<String, Object> customMap;

    /**
     * 自定义xml文件输出目录
     */
    private String xmlPosDir;

    /**
     * 是否生成jpa逆向建表语句
     */
    private boolean useJpa;

    public AutoCodeDto() {
    }
}
