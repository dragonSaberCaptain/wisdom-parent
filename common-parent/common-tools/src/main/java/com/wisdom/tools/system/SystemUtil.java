package com.wisdom.tools.system;

import com.wisdom.tools.file.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @version 1.0
 * @apiNote 与系统相关工具类
 * @date 2021/7/5 15:54 星期一
 */
public class SystemUtil {

    public static void main(String[] args) {
        List<String> list = getPackageAndClassesName("com.wisdom.tools.enums");
        for (String name : list) {
            System.out.println(name);
        }
    }

    /**
     * 返回包下所有的类名
     *
     * @param packagePath 包名全路径
     */
    public static List<String> getClassesName(String packagePath) {
        return getPackageClassesName(packagePath, true, true);
    }

    public static List<String> getPackageAndClassesName(String packagePath) {
        return getPackageClassesName(packagePath, false, true);
    }

    /**
     * @param packagePath 包名全路径
     */
    public static List<String> getPackageClassesName(String packagePath, boolean notPath, boolean notClass) {
        List<String> filesPath = getFilesPath(packagePath);

        List<String> resultList = new ArrayList<>();

        int startIndex = 0;

        String curStr;
        for (String className : filesPath) {
            if (notPath) {
                startIndex = className.lastIndexOf("/");
                if (startIndex == -1) {
                    startIndex = className.lastIndexOf("\\");
                }
                startIndex += 1;
            } else {
                className = className.replace("\\", ".");
            }

            if (notClass) {
                curStr = className.substring(startIndex, className.lastIndexOf("."));
            } else {
                curStr = className.substring(startIndex);
            }

            resultList.add(curStr);
        }
        return resultList;
    }

    /**
     * 已文件路径的形式,返回包下的所有类
     *
     * @param packagePath 包名全路径
     */
    public static List<String> getFilesPath(String packagePath) {
        String filePath = ClassLoader.getSystemResource("").getPath() + packagePath.replace(".", "\\");
        List<File> fileList = FileUtil.getFilesByPath(filePath);

        List<String> resultFileList = new ArrayList<>();
        for (File file : fileList) {
            String subFilePath = file.getPath();
            subFilePath = subFilePath.substring(subFilePath.indexOf("classes") + 8);
            resultFileList.add(subFilePath);
        }
        return resultFileList;
    }
}
