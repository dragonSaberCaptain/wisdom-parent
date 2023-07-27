package com.wisdom.core.dto;

import com.wisdom.tools.dto.CustomAddExt;
import com.wisdom.tools.dto.DbInfoEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * 自动生成代码dto对象
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/10/27 10:34 星期三
 */
@Data
@Accessors(chain = true)
public class AutoCodeDto {
    /**
     * 数据库相关信息
     */
    private DbInfoEntity dbInfoEntity;

    /**
     * 自定义扩展属性,velocity模板使用
     */
    private CustomAddExt customAddExt;

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
     * 生成文件的扩展输出目录
     */
    private String outputExtDir;

    /**
     * 要去除的表前缀： 如定义sys 则sys_user 生成后为:user
     */
    private String tablePrefix;

    /**
     * 要去除的字段前缀： 如定义t 则t_id 生成后为:id
     */
    private String fieldPrefix;

    /**
     * 需要包含的表名，允许正则表达式（与exclude二选一配置）默认:全部
     */
    private String include;

    /**
     * 需要包含的表名，允许正则表达式（与include二选一配置）
     */
    private String exclude;

    /**
     * 自定义xml文件输出目录
     */
    private String xmlPosDir;

    /**
     * 主体 是否覆盖文件
     */
    private Boolean fileOverride;

    /**
     * 是否生成swagge相关注解
     */
    private Boolean openSwagger = false;
}
