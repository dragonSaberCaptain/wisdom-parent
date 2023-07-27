package com.wisdom.tools.translate;

import cn.hutool.json.JSONUtil;
import com.wisdom.tools.dto.DbInfoEntity;
import com.wisdom.tools.translate.baidu.TransApi;
import com.wisdom.tools.translate.baidu.TransResult;
import com.wisdom.tools.translate.baidu.TranslateData;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.driver.OracleDriver;

import java.sql.Connection;
import java.sql.Driver;
import java.util.List;
import java.util.Properties;

/**
 * connection.createStatement() / connection.prepareStatement(sql)
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/2/7 10:20 星期二
 */
@Slf4j
@Data
@Accessors(chain = true)
public class CommonMethod {
    public static Connection connection;

    public static Connection initDb(String path) throws Exception {
        DbInfoEntity dbInfoEntity = new DbInfoEntity(path);
        return initDb(dbInfoEntity.getDriverName(), dbInfoEntity.getUrlDb(), dbInfoEntity.getUsername(), dbInfoEntity.getPassword());
    }

    public static Connection initDb(String driverDb, String urlDb, String userDb, String passwordDb) throws Exception {
        if (null == connection) {
            Driver driver;
            if ("oracle.jdbc.OracleDriver".equalsIgnoreCase(driverDb)) {
                driver = new OracleDriver();
            } else if ("com.mysql.cj.jdbc.Driver".equalsIgnoreCase(driverDb)) {
                driver = new com.mysql.cj.jdbc.Driver();
            } else {
                driver = new com.mysql.jdbc.Driver();
            }
            Properties properties = new Properties();
            properties.setProperty("user", userDb);
            properties.setProperty("password", passwordDb);
            connection = driver.connect(urlDb, properties);
        }
        return connection;
    }

    public static String transDeal(String str) throws Exception {
        String transResult1 = TransApi.getTransResult(str, "zh", "en");
        TransResult transResult = JSONUtil.toBean(transResult1, TransResult.class);
        List<TranslateData> data = transResult.getTrans_result();
        if (data.size() > 0) {
            str = transResult.getTrans_result().get(0).getDst();
        }
        Thread.sleep(1500);
        return str;
    }
}
