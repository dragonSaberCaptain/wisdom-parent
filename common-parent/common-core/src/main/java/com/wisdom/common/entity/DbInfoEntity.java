package com.wisdom.common.entity;

import lombok.Data;
import lombok.experimental.Accessors;

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
        this.driverName = "com.mysql.cj.jdbc.Driver";
        this.urlDb = "jdbc:mysql://localhost:3306/wisdom?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=GMT%2B8";
        this.username = "root";
        this.password = "Yj%Hh+Vtc7n#@captain1023.com";
    }
}
