package com.wisdom.tools.file;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @version 1.0
 * @apiNote 文件操作相关工具类
 * @dateTime 2021/7/5 16:39 星期一
 */
@Slf4j
public class FileUtil {

    public static void main(String[] args) {
        List<File> fileList = getFilesByPath("D:\\BaiduNetdiskDownload\\IdeaProjects\\wisdom", "java");
        for (File file : fileList) {
            System.out.println(file.getName());
            System.out.println(file.getPath());
        }
//        List<String> lineList = readLineByTxt("D:\\captain\\9004个常用汉字列表.txt");
//        for (String str : lineList) {
//            String[] split = str.split(" ");
//            for (String subStr : split) {
//                System.out.println(subStr);
//            }
//        }
    }

    public static List<File> getFilesByPath(String fileSourcePath) {
        return getFilesByPath(fileSourcePath, "*");
    }

    public static List<File> getFilesByPath(String fileSourcePath, String fileType) {
        List<File> fileList = new ArrayList<>();
        File fileDir = new File(fileSourcePath);
        if (fileDir.exists()) {
            File[] files = fileDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        fileList.addAll(getFilesByPath(file.getAbsolutePath(), fileType));
                    } else {
                        if (StrUtil.isEmpty(fileType) || file.getName().endsWith(fileType) || "*".equals(fileType)) {
                            fileList.add(file);
                        }
                    }
                }
            }
        }
        return fileList;
    }

    /**
     * 读取txt文件的内容
     */
    public static List<String> readLineByTxt(String fileSourcePath) {
        List<String> lineList = new ArrayList<>();

        File fileDir = new File(fileSourcePath);

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "gbk"));
            String readStr;
            while ((readStr = br.readLine()) != null) {
                lineList.add(readStr);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lineList;
    }
}
