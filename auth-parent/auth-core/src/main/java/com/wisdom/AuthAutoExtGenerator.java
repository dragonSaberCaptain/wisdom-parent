package com.wisdom;

import com.wisdom.common.dto.AutoCodeDto;
import com.wisdom.common.tools.mybatisplus.MybatisplusUtil;
import com.wisdom.tools.datetime.DateUtilByZoned;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * 认证服务自动生成代码工具类
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/15 16:26 星期三
 */
public class AuthAutoExtGenerator {
    private static final String moduleName = "auth";

    public static void main(String[] args) {
        AutoCodeDto autoCodeDto = new AutoCodeDto();
        autoCodeDto.setDriverName("com.mysql.cj.jdbc.Driver");
        autoCodeDto.setUrlDb("jdbc:mysql://localhost:3306/wisdom?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=GMT%2B8");
        autoCodeDto.setUsername("root");
        autoCodeDto.setPassword("Yj%Hh+Vtc7n#@captain1023.com");

        autoCodeDto.setInclude("sys_user,sys_role,sys_permission,sys_user_role,sys_role_permission");
        autoCodeDto.setExclude("");
        autoCodeDto.setTablePrefix("");
        autoCodeDto.setFieldPrefix("");

        autoCodeDto.setXmlPosDir("");

        autoCodeDto.setModuleName(moduleName);
        autoCodeDto.setModuleParentPath(AuthAutoExtGenerator.class.getPackageName());

        autoCodeDto.setFileOverride(false); //不覆盖文件
        autoCodeDto.setCloseExt(false); //生成扩展类,用于存放除基础业务的额外扩展业务
        autoCodeDto.setOutputDir(System.getProperty("user.dir") + File.separator + moduleName + "-parent/" + moduleName + "-core/src/main/java");
        MybatisplusUtil.autoCreateCode(autoCodeDto);

    }
}