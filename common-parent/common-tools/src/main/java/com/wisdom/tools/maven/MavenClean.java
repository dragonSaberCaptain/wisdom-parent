package com.wisdom.tools.maven;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;

/**
 * Copyright © 2022 dragonSaberCaptain. All rights reserved.
 * 清除maven仓库目录下 生成的多余文件
 *
 * @author captain
 * @version 1.0
 * @datetime 2022/5/16 15:19 星期一
 */
public class MavenClean {
    private static final Logger log = LoggerFactory.getLogger(MavenClean.class);

    public static void main(String[] args) {
//        ${user:home}/.m2/repository
        //本地仓库地址
        String repoPath = "C:\\Users\\admin\\.m2\\repo";
        //需要删除的文件后缀
        String[] extensions = new String[]{
                "lastUpdated",
                "properties",
                "repositories"
        };
        //获取所有文件进行删除
        Collection<File> listFiles = FileUtils.listFiles(new File(repoPath), extensions, true);
        for (File file : listFiles) {
            file.delete();
        }
    }
}
