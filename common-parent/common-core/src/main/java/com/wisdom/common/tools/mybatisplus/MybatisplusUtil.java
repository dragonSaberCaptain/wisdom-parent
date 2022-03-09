package com.wisdom.common.tools.mybatisplus;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.google.common.base.CaseFormat;
import com.wisdom.common.controller.BaseController;
import com.wisdom.common.dao.BaseDao;
import com.wisdom.common.dto.AutoCodeDto;
import com.wisdom.common.entity.BaseEntity;
import com.wisdom.common.entity.CustomAddExt;
import com.wisdom.common.service.BaseService;
import com.wisdom.common.service.impl.BaseServiceImpl;
import com.wisdom.tools.object.ObjectUtil;
import com.wisdom.tools.string.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * mybatisplus 相关工具类 自动生成代码 controller dao service serviceImpl等
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/13 15:49 星期一
 */
@Slf4j
public class MybatisplusUtil {

    public static void autoCreateCode(AutoCodeDto autoCodeDto) {
        //1、全局配置
        GlobalConfig gc = new GlobalConfig();
        //生成路径(一般都是生成在此项目的src/main/java下面)
        gc.setOutputDir(autoCodeDto.getOutputDir());
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

        //2、数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(autoCodeDto.getDbInfoEntity().getUrlDb());
        dsc.setDriverName(autoCodeDto.getDbInfoEntity().getDriverName());
        dsc.setUsername(autoCodeDto.getDbInfoEntity().getUsername());
        dsc.setPassword(autoCodeDto.getDbInfoEntity().getPassword());

        // 3、包配置
        PackageConfig pc = new PackageConfig();
        if (StringUtil.isNotBlank(autoCodeDto.getModuleName())) {
            pc.setModuleName(autoCodeDto.getModuleName());
        } else {
            pc.setModuleName(null);
        }

        pc.setParent(autoCodeDto.getModuleParentPath());
        pc.setController("controller");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setMapper("dao");
        pc.setEntity("entity");
        pc.setXml("mapper");

        // 4、模板配置
        TemplateConfig tc = new TemplateConfig();

        tc.setController("/templates/controller.java.vm");
        tc.setMapper("/templates/dao.java.vm");
        tc.setService("/templates/service.java.vm");
        tc.setServiceImpl("/templates/serviceImpl.java.vm");
        tc.setEntity("/templates/entity.java.vm");
        tc.setXml("/templates/mapper.xml.vm");

        if (StringUtil.isNotBlank(autoCodeDto.getXmlPosDir())) {
            tc.setXml(null);
        }

        // 5、自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                if (null == autoCodeDto.getCustomAddExt()) {
                    autoCodeDto.setCustomAddExt(new CustomAddExt());
                }
                this.setMap(JSONObject.parseObject(JSONObject.toJSONString(autoCodeDto.getCustomAddExt())));
            }
        };

        // 5.1、自定义输出配置 自定义配置会被优先输出
        List<FileOutConfig> focList = new ArrayList<>();

        if (StringUtil.isNotBlank(autoCodeDto.getXmlPosDir())) {
            if (tc.getXml() != null) {
                FileOutConfig xmlFileOutConfig = new FileOutConfig(tc.getXml()) {
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                        return autoCodeDto.getXmlPosDir();
                    }
                };
                focList.add(xmlFileOutConfig);
            }
        }

        if (focList.size() > 0) {
            cfg.setFileOutConfigList(focList);
        }

        // 6、策略配置
        StrategyConfig strategy = new StrategyConfig();
        // 要去除的表前缀
        if (StringUtil.isNotBlank(autoCodeDto.getTablePrefix())) {
            strategy.setTablePrefix(autoCodeDto.getTablePrefix().split(","));
        }
        //要去除的字段前缀
        if (StringUtil.isNotBlank(autoCodeDto.getFieldPrefix())) {
            strategy.setFieldPrefix(autoCodeDto.getFieldPrefix().split(","));
        }

        if (StringUtil.isNotBlank(autoCodeDto.getInclude())) {
            // 逆向工程使用的表   如果要生成多个,这里可以传入String[]
            strategy.setInclude(autoCodeDto.getInclude().split(","));
        } else {
            // 逆向工程要排除的表   如果要生成多个,这里可以传入String[]
            strategy.setExclude(autoCodeDto.getExclude().split(","));
        }
//        strategy.setSuperEntityColumns("myTest");

        //数据库表映射到实体的命名策略
        strategy.setNaming(NamingStrategy.underline_to_camel);

        //数据库表字段映射到实体的命名策略
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);

        strategy.setSuperControllerClass(BaseController.class);
        strategy.setSuperEntityClass(BaseEntity.class);
        strategy.setSuperServiceClass(BaseService.class);
        strategy.setSuperServiceImplClass(BaseServiceImpl.class);
        strategy.setSuperMapperClass(BaseDao.class.getName());

        //使用restController注解
        strategy.setRestControllerStyle(true);
        //使用lombok
        strategy.setEntityLombokModel(true);
        //是否开启链式模型
        strategy.setChainModel(true);

        //乐观锁字段名称
        strategy.setVersionFieldName("version");
        //删除标记字段名称
        strategy.setLogicDeleteFieldName("del_flag");

        //Boolean类型字段是否移除is前缀
        strategy.setEntityBooleanColumnRemoveIsPrefix(true);

        AutoGenerator mpg = new AutoGenerator();
        mpg.setGlobalConfig(gc);
        mpg.setDataSource(dsc);
        mpg.setPackageInfo(pc);
        mpg.setTemplate(tc);
        mpg.setCfg(cfg);
        mpg.setStrategy(strategy);
        mpg.execute();

        //在执行一次,生成扩展相关文件
        if (autoCodeDto.getCustomAddExt().getOpenExt()) {
            mpg = new AutoGenerator();

            strategy.setSuperControllerClass("");

            gc.setControllerName("%sControllerExt");
            gc.setServiceName("%sServiceExt");
            gc.setServiceImplName("%sServiceImplExt");
            gc.setMapperName("%sDaoExt");
            gc.setXmlName("%sMapperExt");
            gc.setEntityName("%sExt");

            tc.setController("/templates/ext/controllerExt.java.vm");
            tc.setMapper("/templates/ext/daoExt.java.vm");
            tc.setService("/templates/ext/serviceExt.java.vm");
            tc.setServiceImpl("/templates/ext/serviceImplExt.java.vm");
            tc.setEntity("/templates/ext/entityExt.java.vm");
            tc.setXml("templates/ext/mapperExt.xml.vm");

            // 生成的扩展文件不覆盖原来已存在的文件
            gc.setFileOverride(false);
            if(StringUtil.isNotBlank(autoCodeDto.getOutputExtDir())){
                gc.setOutputDir(autoCodeDto.getOutputExtDir());
            }

            mpg.setGlobalConfig(gc);
            mpg.setDataSource(dsc);
            mpg.setPackageInfo(pc);
            mpg.setTemplate(tc);
            mpg.setCfg(cfg);
            mpg.setStrategy(strategy);
            mpg.execute();
        }
    }

    /**
     * 将实体类解析成 Mybatisplus 需要的QueryWrapper类
     *
     * @param entity 实体类
     * @author captain
     * @datetime 2021-10-27 10:12:37
     */
    public static <T> QueryWrapper<T> createWrapper(T entity) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        Map<String, Object> fieldMap = ObjectUtil.getDeclaredField(entity);
        for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value == null || "serialVersionUID".equalsIgnoreCase(key) || "SERIAL_VERSION_U_I_D".equalsIgnoreCase(key)) {
                continue;
            }

            //驼峰转大写下划线, userName -> USER_NAME
            String dbKey = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, key);

            if ("CREATE_DATE_TIME_START".equalsIgnoreCase(dbKey)) {
                wrapper.ge("CREATE_TIME", value);
            } else if ("CREATE_DATE_TIME_END".equalsIgnoreCase(dbKey)) {
                wrapper.le("CREATE_TIME", value);
            } else if ("UP_DATE_TIME_START".equalsIgnoreCase(dbKey)) {
                wrapper.ge("UPDATE_TIME", value);
            } else if ("UP_DATE_TIME_END".equalsIgnoreCase(dbKey)) {
                wrapper.le("UPDATE_TIME", value);
            } else {
                wrapper.eq(dbKey, value);
            }
        }
        return wrapper;
    }
}
