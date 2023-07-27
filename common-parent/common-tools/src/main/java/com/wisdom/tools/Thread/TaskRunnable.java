package com.wisdom.tools.Thread;

import cn.hutool.core.util.StrUtil;
import com.wisdom.tools.blast.Blast7ZUtil;

import java.io.File;

/**
 * TODO (用一句话描述该类的作用)
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/1/31 15:04 星期二
 */
public class TaskRunnable implements Runnable {
    private final File srcFile; //文件源
    private final long startIndex; //开始位置
    private final long endIndex; //结束位置

    public TaskRunnable(File srcFile, long startIndex, long endIndex) {
        this.srcFile = srcFile;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public void run() {
        for (long i = startIndex; i <= endIndex; i++) {
            if (Blast7ZUtil.bl) {
                break;
            }
            System.out.println("破解中。。。当前密码:" + i);
            String resultPwd = Blast7ZUtil.un7zCheckPwd(srcFile, i);
            if (StrUtil.isNotBlank(resultPwd)) {
                System.out.println("正确密码:" + resultPwd);
                Blast7ZUtil.bl = true;
            }
        }
    }
}
