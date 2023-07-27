package com.wisdom.tools.translate;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.wisdom.tools.translate.baidu.FileUtil;
import com.wisdom.tools.translate.baidu.SnowflakeIdWorker;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

/**
 * 百度翻译工具类 之 读取JSON 写入到数据库
 *
 * @author captain
 * @version 1.0
 * @datetime 2022/10/28 10:44 星期五
 */
public class ReadJsonWriteToDb {
    //{"from":"zh","to":"en","trans_result":[{"src":"\u9ad8\u5ea6600\u7c73","dst":"Height 600m"}]}

    final static String SOURCE_PATH = "C:\\Users\\admin\\Desktop\\demo\\你的项目根路径\\";
    final static String FILE_TYPE = ".json";

    public static void main(String[] args) throws Exception {
        System.out.println("翻译开始。。。。。。。");
        Connection connection = CommonMethod.initDb("");

        String sql = "insert into CARD_PAR_PRINTERFORMAT_LOCALE(ID,PRINTERFORMAT_ID,PRINTERFORMAT_VALUE,PRINTERFORMAT_LOCALE,GMT_CREATE,GMT_MODIFIED) VALUES (?,?,?,?,?,?,?)";

        List<File> files = FileUtil.getFiles(new File(SOURCE_PATH), FILE_TYPE);
        for (File file : files) {
            String content = FileUtil.file2String(file, "utf-8");
            if (".json".equals(FILE_TYPE)) {
                Map map = JSONUtil.toBean(content, Map.class);
                List<Map<String, String>> recordsList = (List<Map<String, String>>) map.get("RECORDS");
                for (Map<String, String> subMap : recordsList) {
                    String printerFormatLocale = subMap.get("PRINTERFORMAT_LOCALE");
                    if (!printerFormatLocale.equals("zh_CN")) {
                        continue;
                    }
                    String printerFormatValue = subMap.get("PRINTERFORMAT_VALUE");

                    SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(30, 30);
                    String id = String.valueOf(snowflakeIdWorker.nextId());
                    String printerFormatId = subMap.get("PRINTERFORMAT_ID");
                    String gmtCreate = subMap.get("GMT_CREATE");
                    String gmtModified = subMap.get("GMT_MODIFIED");

                    if (StrUtil.isNotBlank(printerFormatValue)) {
                        printerFormatValue = CommonMethod.transDeal(printerFormatValue);
                    }

                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, id);
                    preparedStatement.setString(2, printerFormatId);
                    preparedStatement.setString(3, printerFormatValue);
                    preparedStatement.setString(4, "en_US");
                    preparedStatement.setString(5, gmtCreate);
                    preparedStatement.setString(6, gmtModified);
                    preparedStatement.executeUpdate();
                }
            }
        }
        System.out.println("翻译结束。。。。。。。");
    }
}
