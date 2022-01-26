package com.wisdom;

import com.wisdom.common.dto.AutoCodeDto;
import com.wisdom.common.tools.mybatisplus.MybatisplusUtil;
import com.wisdom.tools.datetime.DateUtilByZoned;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * 例子:服务自动生成代码工具类-模板
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/15 16:26 星期三
 */

@Slf4j
public class ExampleCodeGenerator {
    private static final String moduleName = "example";
    private static final String packageName = ExampleCodeGenerator.class.getPackageName();

    public static void main(String[] args) {
        AutoCodeDto autoCodeDto = new AutoCodeDto();
        autoCodeDto.setDriverName("com.mysql.cj.jdbc.Driver");
        autoCodeDto.setUrlDb("jdbc:mysql://localhost:3306/wisdom?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=GMT%2B8");
        autoCodeDto.setUsername("填实际的数据库账号");
        autoCodeDto.setPassword("填实际的数据库密码");

        autoCodeDto.setInclude("user,test");
        autoCodeDto.setExclude("");
        autoCodeDto.setTablePrefix("");
        autoCodeDto.setFieldPrefix("");

        autoCodeDto.setOutputDir(System.getProperty("user.dir") + File.separator + moduleName + "-parent/" + moduleName + "-generation/src/main/java");
        autoCodeDto.setOutputExtDir(System.getProperty("user.dir") + File.separator + moduleName + "-parent/" + moduleName + "-core/src/main/java");

        autoCodeDto.setXmlPosDir("");

        autoCodeDto.setModuleName(moduleName);
        autoCodeDto.setModuleParentPath(packageName);

        autoCodeDto.setOpenExt(true);

        MybatisplusUtil.autoCreateCode(autoCodeDto);
    }
}


