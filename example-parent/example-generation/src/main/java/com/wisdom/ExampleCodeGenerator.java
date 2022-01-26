package com.wisdom;

import com.wisdom.common.dto.AutoCodeDto;
import com.wisdom.common.tools.mybatisplus.MybatisplusUtil;
import com.wisdom.tools.datetime.DateUtilByZoned;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * 例子服务自动生成代码工具类-模板
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/15 16:26 星期三
 */

@Slf4j
public class ExampleCodeGenerator {
    private static final String moduleName = "example";
    private static final String packageName = ExampleAutoGenerator.class.getPackageName();

    public static void main(String[] args) {
        AutoCodeDto autoCodeDto = new AutoCodeDto();

        autoCodeDto.setDriverName("com.mysql.cj.jdbc.Driver");
        autoCodeDto.setUrlDb("jdbc:mysql://localhost:3306/wisdom?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=GMT%2B8");
        autoCodeDto.setUsername("root");
        autoCodeDto.setPassword("xxxxxxxxxxx");

        autoCodeDto.setInclude("user,test");
        autoCodeDto.setExclude("");
        autoCodeDto.setTablePrefix("");
        autoCodeDto.setFieldPrefix("");

        autoCodeDto.setOutputDir(System.getProperty("user.dir") + "/" + moduleName + "-parent/" + moduleName + "-generation/src/main/java");
        autoCodeDto.setXmlPosDir("");

        autoCodeDto.setModuleName(moduleName);
        autoCodeDto.setModuleParentPath(packageName);

        Map<String, Object> customMap = new HashMap<>();

        ZonedDateTime zonedDateTime = DateUtilByZoned.now();
        String patternNow = DateUtilByZoned.getDateTime();
        String dayOfWeekCn = DateUtilByZoned.getDayOfWeekCn(zonedDateTime);
        customMap.put("version", "1.0");
        customMap.put("createDate", patternNow + " " + dayOfWeekCn);
        customMap.put("year", zonedDateTime.getYear());
        customMap.put("useJpa", true);

        autoCodeDto.setCustomMap(customMap);

        MybatisplusUtil.autoCreateCode(autoCodeDto);
    }
}


