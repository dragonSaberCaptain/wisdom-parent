package com.wisdom;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.wisdom.common.controller.BaseController;
import com.wisdom.common.dao.BaseDao;
import com.wisdom.common.entity.BaseEntity;
import com.wisdom.common.service.BaseService;
import com.wisdom.common.service.impl.BaseServiceImpl;
import com.wisdom.tools.datetime.DateUtilByZoned;
import com.wisdom.tools.string.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * 例子服务自动生成代码工具类
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/15 16:26 星期三
 */
public class ExampleCodeGenerator {
    private static final String driverName = "com.mysql.cj.jdbc.Driver";
    private static final String urlDb = "jdbc:mysql://localhost:3306/wisdom?characterEncoding=utf8&connectTimeout=3000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=GMT%2B8";
    private static final String username = "root";
    private static final String password = "xxxx"; //换成数据库的密码

    //模块名称
    private static final String moduleName = "example";

    //项目名称: 如 example-parent/example-generation
    private static final String projectName = moduleName + "-parent/" + moduleName + "-generation";

    //需要生成的表名，允许正则表达式
    private static final String include = "user,test";

    //需要排除的表名，允许正则表达式
    private static final String exclude = "";

    //路径
    private static final String parent = "com.wisdom";

    //要去除的表前缀： 如定义sys 则sys_user 生成后为:user
    private static final String tablePrefix = "";

    //要去除的字段前缀： 如定义t 则t_id 生成后为:id
    private static final String fieldPrefix = "";

    //项目路径
    private static final String projectPath = System.getProperty("user.dir");

    //生成文件的输出目录
    private static final String outputDir = "src/main/java";

    //生成的xml在默认位置,即 resources/mapper/ 文件夹下
    private static boolean defaultXmlPos = false;

    //是否使用jpa
    private static boolean useJpa = true;

    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();

        //1、全局配置
        GlobalConfig gc = new GlobalConfig();
        //生成路径(一般都是生成在此项目的src/main/java下面)
        gc.setOutputDir(projectPath + "/" + projectName + "/" + outputDir);
        //是否使用swagger2
        gc.setSwagger2(true);
        //是否覆盖文件
        gc.setFileOverride(true);
        // 主键策略
        gc.setIdType(IdType.ASSIGN_ID);
        //数据库时间类型 到 实体类时间类型 对应策略
        gc.setDateType(DateType.ONLY_DATE);
        // 不需要ActiveRecord特性的请改为false
        gc.setActiveRecord(false);
        // XML 二级缓存 开启前提是保证crud在同一个命名空间下,即一表对应一个xml
        gc.setEnableCache(false);
        // 生成resultMap
        gc.setBaseResultMap(true);
        // 生成基本的SQL片段
        gc.setBaseColumnList(true);
        //设置作者
        gc.setAuthor("captain");
        //是否打开输出目录
        gc.setOpen(false);

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setControllerName("%sController");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setMapperName("%sDao");
        gc.setXmlName("%sMapper");
        mpg.setGlobalConfig(gc);

        //2、数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(urlDb);
        dsc.setDriverName(driverName);
        dsc.setUsername(username);
        dsc.setPassword(password);
        mpg.setDataSource(dsc);

        // 3、包配置
        PackageConfig pc = new PackageConfig();
        if (StringUtil.isNotBlank(moduleName)) {
            pc.setModuleName(moduleName);
        } else {
            pc.setModuleName(null);
        }

        pc.setParent(parent);
        pc.setController("controller");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setMapper("dao");
        pc.setEntity("entity");
        pc.setXml("mapper");
        mpg.setPackageInfo(pc);

        // 4、模板配置
        TemplateConfig tc = new TemplateConfig();
        tc.setController("/templates/controller.java.vm");
        tc.setEntity("/templates/entity.java.vm");
        tc.setMapper("/templates/dao.java.vm");
        tc.setService("/templates/service.java.vm");
        tc.setServiceImpl("/templates/serviceImpl.java.vm");
        if (defaultXmlPos) {
            tc.setXml(null);
        } else {
            tc.setXml("/templates/mapper.xml.vm");
        }


        mpg.setTemplate(tc);

        // 5、自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                ZonedDateTime zonedDateTime = DateUtilByZoned.now();
                String patternNow = DateUtilByZoned.getDateTime();
                String dayOfWeekCn = DateUtilByZoned.getDayOfWeekCn(zonedDateTime);
                int year = zonedDateTime.getYear();
                map.put("version", "1.0");
                map.put("createDate", patternNow + " " + dayOfWeekCn);
                map.put("year", year);
                map.put("useJpa", useJpa);
                this.setMap(map);
            }
        };

        // 5.1、自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/" + projectName + "/src/main/resources/mapper/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        if (defaultXmlPos) {
            cfg.setFileOutConfigList(focList);
        }

        mpg.setCfg(cfg);

        // 6、策略配置
        StrategyConfig strategy = new StrategyConfig();
        // 要去除的表前缀
        strategy.setTablePrefix(tablePrefix);
        //要去除的字段前缀
        strategy.setFieldPrefix(fieldPrefix);

        if (StringUtils.isNotBlank(include)) {
            // 逆向工程使用的表   如果要生成多个,这里可以传入String[]
            strategy.setInclude(include.split(","));
        } else {
            // 逆向工程要排除的表   如果要生成多个,这里可以传入String[]
            strategy.setExclude(exclude.split(","));
        }

        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);

        strategy.setSuperControllerClass(BaseController.class);
        strategy.setSuperEntityClass(BaseEntity.class);
        strategy.setSuperServiceClass(BaseService.class);
        strategy.setSuperServiceImplClass(BaseServiceImpl.class);
        strategy.setSuperMapperClass(BaseDao.class.getName());

        //使用restController注解
        strategy.setRestControllerStyle(false);
        //使用lombok
        strategy.setEntityLombokModel(true);

        //Boolean类型字段是否移除is前缀
        strategy.setEntityBooleanColumnRemoveIsPrefix(true);

        mpg.setStrategy(strategy);
        mpg.execute();
    }
}