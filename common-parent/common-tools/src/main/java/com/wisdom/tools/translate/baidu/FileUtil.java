package com.wisdom.tools.translate.baidu;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件处理工具类
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/2/3 16:54 星期五
 */

public class FileUtil {
    public static final int BUFF_SIZE = 10240;

    /**
     * 遍历path目录，获取全部的匹配文件，并将其放入到fileList列表里面；
     *
     * @param path 路径
     * @author captain
     * @datetime 2022-12-28 14:30:11
     */
    public static List<File> getFiles(File path, String fileType) {
        List<File> fileList = new ArrayList<>();
        File[] files = path.listFiles();
        for (File file : files)
            if (file.isDirectory() && !file.isHidden()) {
                fileList.addAll(getFiles(file, fileType));
            } else if (file.getName().toLowerCase().endsWith(fileType)) {
                fileList.add(file);
                System.out.println("需国际化的" + fileType + "文件:" + file.getAbsolutePath());
            }
        return fileList;
    }

    /**
     * 读取文件内容，返回字符串；
     *
     * @param file     文件
     * @param encoding 编码
     * @author captain
     * @datetime 2022-12-19 13:49:22
     */
    public static String file2String(File file, String encoding) {
        StringWriter writer = new StringWriter();
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file), encoding);
            char[] buffer = new char[BUFF_SIZE];
            int n;
            while (-1 != (n = reader.read(buffer))) {
                writer.write(buffer, 0, n);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    public static File getFilesByAbsolutePath(String absolutePath) {
        return new File(absolutePath);
    }

    public static List<File> getFilesByRelativePath(String relativePath, String fileType) {
        return getFiles(new File(relativePath), fileType);
    }
}
