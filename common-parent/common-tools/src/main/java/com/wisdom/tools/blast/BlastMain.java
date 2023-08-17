/*
 * Copyright (c) 2023 dragonSaberCaptain.All rights reserved.
 * 当前项目名:wisdom-parent
 * 当前模块名:common-tools
 * 当前文件的权限定名:com.wisdom.tools.blast.BlastMain
 * 当前文件的名称:BlastMain.java
 * 当前文件的类名:BlastMain
 * 上一次文件修改的日期时间:2023/8/17 下午2:42
 *
 */

package com.wisdom.tools.blast;

import com.wisdom.tools.Thread.TaskRunnable;
import com.wisdom.tools.Thread.ThreadUtil;
import com.wisdom.tools.string.StringUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO (用一句话描述该类的作用)
 *
 * @author captain
 * @version 1.0
 * @projectName wisdom-parent
 * @packageName com.wisdom.tools.blast
 * @dateTime 2023/8/14 13:05 星期一
 */
@Slf4j
@Data
@Accessors(chain = true)
public class BlastMain {

    public static void main(String[] args) throws Exception {
        log.info("破解中,请稍后。。。");//花费分钟数

        String filePath = "C:\\Users\\admin\\Desktop\\mosaic\\mosaic.zip";
        //获取当前压缩文件
        File srcFile = new File(filePath);

        List<String> numberStr = StringUtil.getNumberStr(4, 4);
        //List<String> numberStr = StringUtil.getAlphabetStr(4, 4);
        log.info("密码本大小:" + numberStr.size());

        //计算启用的线程的核心数(速度优化之-以硬件换时间)
        //int corePoolSize = ThreadUtil.getExecutor().getCorePoolSize(); //全力破解(cpu占满)
        int corePoolSize = ThreadUtil.getExecutor().getCorePoolSize() / 2; //普通破解(cpu占一半)
        //int corePoolSize = ThreadUtil.getExecutor().getCorePoolSize() / 3; //低速破解(占cpu三分之一)

        //文件大小
        long fileUsableSpace = FileUtils.sizeOf(srcFile);
        //当前文件夹剩余可用空间
        long usableSpace = srcFile.getParentFile().getUsableSpace();

        //计算是否启动临时文件提高破解速度(速度优化之-以硬件换时间)
        boolean openBlastCopyTmpFile = usableSpace > fileUsableSpace * (corePoolSize + 1);

        List<File> tmpFileList = new ArrayList<>();
        if (srcFile.exists()) {
            String fileType = srcFile.getPath().substring(srcFile.getPath().lastIndexOf("."));

            int sliceSize = numberStr.size() / corePoolSize; //计算每个线程处理数 124
            int rem = numberStr.size() % corePoolSize; // 待处理剩余数

            for (int i = 0; i <= corePoolSize; i++) {
                int startIndex = i * sliceSize;
                int endIndex;
                if (i == corePoolSize) {
                    if (rem == 0) {
                        break;
                    } else {
                        endIndex = startIndex + rem;
                    }
                } else {
                    endIndex = (i + 1) * sliceSize;
                }

                File tmpFile = srcFile;
                if (openBlastCopyTmpFile) {
                    String tmpFilePath = srcFile.getPath().substring(0, srcFile.getPath().lastIndexOf(".")) + "tmp" + (i + 1) + fileType;
                    tmpFile = new File(tmpFilePath);
                    if (!tmpFile.exists()) {
                        Files.copy(srcFile.toPath(), tmpFile.toPath());
                        Runtime.getRuntime().exec("attrib +H " + tmpFile.getPath());
                    }
                    tmpFileList.add(tmpFile);
                }

                log.info("开始位置:" + startIndex + ",结束位置:" + endIndex);
                TaskRunnable taskRunnable = new TaskRunnable(tmpFile, numberStr.subList(startIndex, endIndex));
                ThreadUtil.execute(taskRunnable);

            }
        }

        while (true) {
            int activeCount = ThreadUtil.getExecutor().getActiveCount(); //获取未完全停止的线程数
            if (activeCount <= 0) { //线程完全停止后,删除临时文件
                for (File tmpFile : tmpFileList) {
                    if (tmpFile.exists()) {
                        tmpFile.delete();
                    }
                    String tmpFilePath = tmpFile.getPath().substring(0, tmpFile.getPath().lastIndexOf("."));
                    File tmpFileDirectory = new File(tmpFilePath);
                    if (tmpFileDirectory.exists()) {
                        try {
                            FileUtils.deleteDirectory(new File(tmpFilePath));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                break;
            } else {
                ThreadUtil.shutdownNow(); //立即停止所有线程
            }
        }
    }
}
