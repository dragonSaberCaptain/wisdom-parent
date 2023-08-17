/*
 * Copyright (c) 2023 dragonSaberCaptain.All rights reserved.
 * 当前项目名:wisdom-parent
 * 当前模块名:common-tools
 * 当前文件的权限定名:com.wisdom.tools.Thread.TaskRunnable
 * 当前文件的名称:TaskRunnable.java
 * 当前文件的类名:TaskRunnable
 * 上一次文件修改的日期时间:2023/8/17 下午2:43
 *
 */

package com.wisdom.tools.Thread;

import com.wisdom.tools.blast.CompressFileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

/**
 * TODO (用一句话描述该类的作用)
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/1/31 15:04 星期二
 */
@Slf4j
public class TaskRunnable implements Runnable {
    public static volatile boolean open_exit = false;

    private final File srcFile; //文件源

    private final List<String> pwdList; //密码列表

    public TaskRunnable(File srcFile, List<String> pwdList) {
        this.srcFile = srcFile;
        this.pwdList = pwdList;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        for (String pwd : pwdList) {
            synchronized (this) {
                if (!open_exit) {
                    boolean bl = CompressFileUtil.unZipCheckPwd(srcFile, pwd);
                    if (bl) {
                        open_exit = true;
                        log.info("线程：" + name + ",尝试密码成功。。。。。。密码:" + pwd);
                        ThreadUtil.shutdownNow();//立即停止所有线程
                    }
                }
            }
        }
    }
}
