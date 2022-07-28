package com.wisdom.config.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;


/**
 * Copyright © 2022 dragonSaberCaptain. All rights reserved.
 * <p>
 * 系统的启动信息实体类
 *
 * @author captain
 * @version 1.0
 * @datetime 2022/6/27 9:56 星期一
 */
@Data
@Slf4j
@Accessors(chain = true)
public class SystemInfoDto {
    /**
     * 操作系统名称
     */
    private String osName;
    /**
     * 操作系统处理器
     */
    private String osArch;

    /**
     * 操作系统版本
     */
    private String osVersion;

    /**
     * java vm 规范版本
     */
    private String javaVmSpecificationVersion;
    /**
     * java vm 规范供应商名称
     */
    private String javaVmSpecificationVendor;

    /**
     * java vm 规范名称
     */
    private String javaVmSpecificationName;
    /**
     * java vm 版本
     */
    private String javaVmVersion;
    /**
     * java vm 名称
     */
    private String javaVmName;
    /**
     * java vm 供应商
     */
    private String javaVmVendor;
    /**
     * java 版本
     */
    private String javaVersion;
    /**
     * 算法提供者
     */
    private String providers;
    /**
     * 启动类
     */
    private String name;
    /**
     * 简称
     */
    private String simpleName;
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 端口
     */
    private String port;
    /**
     * 激活的配置
     */
    private String profilesActive;
    /**
     * 在系统中的pid
     */
    private String pid;
    /**
     * 进程的用户
     */
    private String user;
    /**
     * 进程参数的字符串数组
     */
    private String[] arguments;
    /**
     * 进程的可执行路径名
     */
    private String command;

    /**
     * 进程的命令行
     */
    private String commandLine;

    /**
     * 进程的开始时间
     */
    private Instant startInstant;
    /**
     * 进程使用的CPU时间
     */
    private Duration totalCpuDuration;
}
