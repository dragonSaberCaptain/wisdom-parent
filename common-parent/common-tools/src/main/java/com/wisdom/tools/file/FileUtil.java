package com.wisdom.tools.file;

import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @version 1.0
 * @apiNote 文件操作相关工具类
 * @date 2021/7/5 16:39 星期一
 */
@Slf4j
public class FileUtil {

    public static void main(String[] args) {
        List<File> fileList = getFilesByPath("D:\\BaiduNetdiskDownload\\IdeaProjects\\wisdom");
        for (File file : fileList) {
            System.out.println(file.getName());
            System.out.println(file.getPath());
        }
    }

    public static List<File> getFilesByPath(String fileSourcePath) {
        List<File> fileList = new ArrayList<>();
        File fileDir = new File(fileSourcePath);
        if (fileDir.exists()) {
            File[] files = fileDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        fileList.addAll(getFilesByPath(file.getAbsolutePath()));
                    } else {
                        fileList.add(file);
                    }
                }
            }
        }
        return fileList;
    }
}
