package com.wisdom.tools.Thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具类
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/1/31 14:55 星期二
 */
public class ThreadUtil {
    /**
     * 线程等待回收的存活时间，单位：分钟
     */
    private static final long keepAliveTime = 10;
    /**
     * 线程池实例
     */
    private static final ThreadPoolExecutor executor;
    /**
     * 核心线程数
     */
    public static Integer corePoolSize = Runtime.getRuntime().availableProcessors();
    /**
     * 最大线程数
     */
    public static Integer maximumPoolSize = corePoolSize * 2;

    /*
      初始化线程池
      线程池拒绝策略为默认的拒绝策略，如果不能加入工作队列就抛出RejectedExecutionException异常
     */
    static {
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MINUTES, new LinkedBlockingDeque<>(1000), new ThreadFactoryBuilder().setNameFormat("default-pool-%d").build(), new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 使用线程池运行任务，线程无返回值
     */
    public static void execute(Runnable task) {
        executor.execute(task);
    }

    /**
     * 停止线程池
     */
    public static void shutdown() {
        executor.shutdown();
    }

    /**
     * 立即停止线程池
     */
    public static void shutdownNow() {
        executor.shutdownNow();
    }

    /**
     * 获取线程池实例
     */
    public static ThreadPoolExecutor getExecutor() {
        return executor;
    }

    public static Integer getCorePoolSize() {
        return corePoolSize;
    }

    public static Integer getMaximumPoolSize() {
        return maximumPoolSize;
    }

}
