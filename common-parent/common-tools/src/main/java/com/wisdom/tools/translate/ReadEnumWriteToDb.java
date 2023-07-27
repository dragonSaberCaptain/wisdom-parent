package com.wisdom.tools.translate;

import cn.hutool.core.util.StrUtil;
import com.wisdom.base.enums.DateTimeEnum;
import com.wisdom.base.enums.ResultEnum;
import com.wisdom.tools.datetime.DateUtilByZoned;
import com.wisdom.tools.translate.baidu.SnowflakeIdWorker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 百度翻译工具类 之 翻译枚举 写入到数据库
 *
 * @author captain
 * @version 1.0
 * @datetime 2022/10/28 10:44 星期五
 */
public class ReadEnumWriteToDb {
    //{"from":"zh","to":"en","trans_result":[{"src":"\u9ad8\u5ea6600\u7c73","dst":"Height 600m"}]}

    final static String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    final static String DB_URL = "jdbc:mysql://localhost:3306/wisdom?characterEncoding=utf-8&useUnicode=true&useSSL=false&serverTimezone=UTC";
    final static String DB_USER = "root";
    final static String DB_PASSWORD = "Yj%Hh+Vtc7n#@captain1023.com";
    private static Connection connection;

    public static void main(String[] args) throws Exception {
        System.out.println("正在翻译。。。");
        connection = CommonMethod.initDb(DB_DRIVER, DB_URL, DB_USER, DB_PASSWORD);

        String dateTime = DateUtilByZoned.getDateTime(DateTimeEnum.DATETIME_PATTERN_UN);
        String sql = "insert into test(ID,CREATE_DATE_TIME,CREATE_ID,DEL_FLAG,REMARK,UP_DATE_TIME,UPDATE_ID,VERSION,TEST_COLUMN) VALUES (?,?,?,?,?,?,?,?,?)";

        ResultEnum[] values = ResultEnum.values();
        for (ResultEnum anEnum : values) {
            String dealStrCn = anEnum.getMsg();
            if (StrUtil.isNotBlank(dealStrCn)) {
                String code = anEnum.getCode();
                String dealStrEn = CommonMethod.transDeal(dealStrCn);
                int size = querySizeSql(code); //入库前先查询看是否存在
                if (size < 1) { //不存在才执行插入
                    System.out.println("正在翻译:code =" + code + " 原文:" + dealStrCn + " 译文: " + dealStrEn);
                    SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(30, 30);

                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, String.valueOf(snowflakeIdWorker.nextId()));
                    preparedStatement.setString(2, dateTime);
                    preparedStatement.setString(3, "ysw");
                    preparedStatement.setString(4, "0");
                    preparedStatement.setString(5, code);
                    preparedStatement.setString(6, dateTime);
                    preparedStatement.setString(7, "ysw");
                    preparedStatement.setString(8, "1");
                    preparedStatement.setString(9, dealStrCn);
                    //preparedStatement.addBatch();
                    preparedStatement.executeUpdate();

                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, String.valueOf(snowflakeIdWorker.nextId()));
                    preparedStatement.setString(2, dateTime);
                    preparedStatement.setString(3, "ysw");
                    preparedStatement.setString(4, "0");
                    preparedStatement.setString(5, code);
                    preparedStatement.setString(6, dateTime);
                    preparedStatement.setString(7, "ysw");
                    preparedStatement.setString(8, "1");
                    preparedStatement.setString(9, dealStrEn);
                    preparedStatement.executeUpdate();
                    //preparedStatement.addBatch();
                }
            }
        }
        System.out.println("翻译完毕！！！");
    }

    public static int querySizeSql(String code) throws Exception {
        String sql = "SELECT * FROM test WHERE REMARK= ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        preparedStatement.setString(1, code);
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean last = resultSet.last();
        int size = 0;
        if (last) {
            size = resultSet.getRow();
        }
        //while (resultSet.next()){//判断是否有下一条数据
        //    //取出数据
        //    System.out.println("学号"+resultSet.getInt("stuId")+ "\t"+"姓名:"+resultSet.getString("stuname"));
        //}
        return size;
    }
}
