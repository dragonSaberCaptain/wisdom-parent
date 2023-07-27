package com.wisdom.tools.dto;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.wisdom.base.enums.ResultEnum;
import com.wisdom.base.exception.ResultException;
import com.wisdom.tools.system.ReaderResUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
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
@Slf4j
@Data
@Accessors(chain = true)
public class DbInfoEntity {
    protected String driverName;
    protected String urlDb;
    protected String username;
    protected String password;

    private String namespace = "public";
    private String serverAddr = "localhost:8848";
    private String group = "DEFAULT_GROUP";

    public DbInfoEntity(String path) {
        try {
            String content = createProperties(path);
            Yaml yaml = new Yaml();

            Map dbConfigMap = yaml.loadAs(content, Map.class);
            Map<String, Object> springMap = (Map<String, Object>) dbConfigMap.get("spring");
            Map<String, Object> datasourceMap = (Map<String, Object>) springMap.get("datasource");

            this.driverName = String.valueOf(datasourceMap.get("driverClassName"));
            this.urlDb = String.valueOf(datasourceMap.get("url"));
            this.username = String.valueOf(datasourceMap.get("username"));
            this.password = String.valueOf(datasourceMap.get("password"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String createProperties(String path) throws NacosException, FileNotFoundException {
        ReaderResUtil readerResUtil = new ReaderResUtil();

        String fileType;
        if (path.lastIndexOf(".") != -1) {
            fileType = path.substring(path.lastIndexOf(".") + 1);
        } else {
            log.error("文件路径错误:" + path);
            throw new ResultException(ResultEnum.RESULT_ENUM_1023);
        }

        readerResUtil.initByPath(path, fileType);

        Properties prop = new Properties();
        namespace = String.valueOf(readerResUtil.getValueByKey("spring.cloud.nacos.config.namespace"));
        serverAddr = String.valueOf(readerResUtil.getValueByKey("spring.cloud.nacos.config.serverAddr"));
        group = String.valueOf(readerResUtil.getValueByKey("spring.cloud.nacos.config.group"));

        prop.put(PropertyKeyConst.NAMESPACE, namespace);
        prop.put(PropertyKeyConst.SERVER_ADDR, serverAddr);

        ConfigService config = NacosFactory.createConfigService(prop);
        return config.getConfig("wisdom.yaml", group, 5000);
    }
}
