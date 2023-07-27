package com.wisdom;

import com.wisdom.core.dto.AutoCodeDto;
import com.wisdom.core.entity.BaseEntity;
import com.wisdom.core.tools.mybatisplus.MybatisplusUtil;
import com.wisdom.tools.dto.CustomAddExt;
import com.wisdom.tools.dto.DbInfoEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;

/**
 * a
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * 例子服务自动生成代码工具类
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/15 16:26 星期三
 */

@Slf4j
public class ExampleAuto {
    private static final String MODULE_NAME = "example";
    private static final String MODULE_FULL_PARENT_NAME = MODULE_NAME + "-parent";
    private static final String MODULE_FULL_CORE_NAME = MODULE_NAME + "-" + "core";
    private static final String MODULE_FULL_GENERATION_NAME = MODULE_NAME + "-" + "auto";
    private static final String PACKAGE_NAME = ExampleAuto.class.getPackage().getName();
    private static final String RES_PATH;
    private static final String GENERATION_JAVA_PATH;
    private static final String CORE_JAVA_PATH;

    static {
        RES_PATH = SystemUtils.USER_DIR + File.separator +
                MODULE_FULL_PARENT_NAME + File.separator +
                MODULE_FULL_CORE_NAME + File.separator +
                "src" + File.separator +
                "main" + File.separator +
                "resources" + File.separator +
                "bootstrap.yaml";

        GENERATION_JAVA_PATH = SystemUtils.USER_DIR + File.separator +
                MODULE_FULL_PARENT_NAME + File.separator +
                MODULE_FULL_GENERATION_NAME + File.separator +
                "src" + File.separator +
                "main" + File.separator +
                "java";

        CORE_JAVA_PATH = SystemUtils.USER_DIR + File.separator +
                MODULE_FULL_PARENT_NAME + File.separator +
                MODULE_FULL_CORE_NAME + File.separator +
                "src" + File.separator +
                "main" + File.separator +
                "java";
    }

    public static void main(String[] args) {
        AutoCodeDto autoCodeDto = new AutoCodeDto();
        autoCodeDto.setDbInfoEntity(new DbInfoEntity(RES_PATH));
        autoCodeDto.setInclude("user,test");
        autoCodeDto.setExclude("");
        autoCodeDto.setTablePrefix("");
        autoCodeDto.setFieldPrefix("");

        autoCodeDto.setOutputDir(GENERATION_JAVA_PATH);
        autoCodeDto.setOutputExtDir(CORE_JAVA_PATH);
        autoCodeDto.setXmlPosDir("");

        autoCodeDto.setModuleName(MODULE_NAME);
        autoCodeDto.setModuleParentPath(PACKAGE_NAME);

        CustomAddExt customAddExt = new CustomAddExt();
        customAddExt.setOpenExt(true);
        customAddExt.setVersion("1.0.0");
        customAddExt.setUseJpa(true);
        customAddExt.setSuperEntityClassPackage(BaseEntity.class.getName());
        customAddExt.setFileOverride(true);
        autoCodeDto.setFileOverride(true);
        autoCodeDto.setOpenSwagger(false);
        autoCodeDto.setCustomAddExt(customAddExt);

        MybatisplusUtil.autoCreateCode(autoCodeDto);
    }
}


