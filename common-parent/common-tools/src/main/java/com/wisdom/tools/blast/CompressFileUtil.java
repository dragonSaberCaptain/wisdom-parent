/*
 * Copyright (c) 2023 dragonSaberCaptain.All rights reserved.
 * 当前项目名:wisdom-parent
 * 当前模块名:common-tools
 * 当前文件的权限定名:com.wisdom.tools.blast.CompressFileUtil
 * 当前文件的名称:CompressFileUtil.java
 * 当前文件的类名:CompressFileUtil
 * 上一次文件修改的日期时间:2023/8/17 下午2:43
 *
 */

package com.wisdom.tools.blast;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 压缩文件工具类 commons-compress 二次封装
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/1/29 13:46 星期日
 */
@Slf4j
@Data
@Accessors(chain = true)
public class CompressFileUtil {
    public static boolean unZipCheckPwd(File srcFile, String pwd) {
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(srcFile);
            if (!zipFile.isValidZipFile()) {
                throw new ZipException("No zip file");
            }
            if (!zipFile.isEncrypted()) {
                throw new ZipException("No compressed password");
            }
            if (StringUtils.isNotBlank(pwd)) {
                zipFile.setPassword(pwd.toCharArray());
            }
            zipFile.setCharset(Charset.forName("GBK")); //处理中文乱码

            String fileName = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));
            String tmpFilePath = srcFile.getParentFile().getAbsolutePath() + File.separator + fileName;
            zipFile.extractAll(tmpFilePath);
            if (zipFile.getBufferSize() > 0) {
                return true;
            }
        } catch (ZipException e) {
            if (!"Wrong Password".equalsIgnoreCase(e.getMessage())) {
                log.error("CompressFileUtil--unZipCheckPwd:", e);
                e.printStackTrace();
            }
        } finally {
            try {
                zipFile.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public static boolean un7zCheckPwd(String srcFile, String pwd) {
        return un7zCheckPwd(new File(srcFile), pwd);
    }

    public static boolean un7zCheckPwd(File srcFile, String pwd) {
        SevenZFile sevenZFile = null;
        try {
            if (StringUtils.isNotBlank(pwd)) {
                sevenZFile = new SevenZFile(srcFile, pwd.toCharArray());
            } else {
                sevenZFile = new SevenZFile(srcFile);
            }
            //SevenZArchiveEntry entry;
            //while ((entry = sevenZFile.getNextEntry()) != null) {
            //    if (!entry.isDirectory()) {
            //        if (sevenZFile.read() == 255) {
            //            break;
            //        } else {
            //            return null;
            //        }
            //    }
            //}
        } catch (Exception e) {
            log.error("CompressFileUtil--un7zCheckPwd:", e);
            e.printStackTrace();
        } finally {
            //TODO
        }
        return false;
    }
}
