package com.wisdom.common.entity;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import lombok.Data;
import lombok.experimental.Accessors;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;
import java.util.Properties;

/**
 * Copyright © 2022 dragonSaberCaptain. All rights reserved.
 * <p>
 * 数据库连接对象
 *
 * @author captain
 * @version 1.0
 * @datetime 2022/2/8 15:28 星期二
 */
@Data
@Accessors(chain = true)
public class DbInfoEntity {
    protected String driverName;
    protected String urlDb;
    protected String username;
    protected String password;

    public DbInfoEntity() {
        getDbConfig();
    }

    public void getDbConfig() {
        try {
            var prop = new Properties();
            prop.put(PropertyKeyConst.NAMESPACE, "public");
            prop.put(PropertyKeyConst.SERVER_ADDR, "127.0.0.1:8850");

            var config = NacosFactory.createConfigService(prop);
            var content = config.getConfig("wisdom.yaml", "WISDOM_GROUP", 5000);
            System.out.println(content);
            var yaml = new Yaml();
            var dbConfigMap = yaml.loadAs(content, Map.class);
            var springMap = (Map<String, Object>) dbConfigMap.get("spring");
            var datasourceMap = (Map<String, Object>) springMap.get("datasource");
            this.driverName = String.valueOf(datasourceMap.get("driverClassName"));
            this.urlDb = String.valueOf(datasourceMap.get("url"));
            this.username = String.valueOf(datasourceMap.get("username"));
            this.password = String.valueOf(datasourceMap.get("password"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
//        new DbInfoEntity();
    }
}
