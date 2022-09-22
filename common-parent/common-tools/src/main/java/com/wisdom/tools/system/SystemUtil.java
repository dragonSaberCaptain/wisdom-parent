package com.wisdom.tools.system;

import com.alibaba.fastjson.JSONObject;
import com.wisdom.config.dto.SystemInfoDto;
import com.wisdom.constant.Constant;
import com.wisdom.tools.file.FileUtil;
import com.wisdom.tools.string.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @version 1.0
 * @apiNote 与系统相关工具类
 * @date 2021/7/5 15:54 星期一
 */
@Slf4j
public class SystemUtil {

    public static void main(String[] args) {
        List<String> list = getPackageClassesName(null, SystemUtil.class, true, true);
        for (String name : list) {
            System.out.println(name);
        }
    }

    /**
     * 获取某个包下的所有文件
     *
     * @param packagePath 包名全路径
     * @param addPath     是否返回 全路径
     * @param addClass    是否返回 .class结尾
     * @return java.util.List<java.lang.String>
     * @author captain
     * @datetime 2022-06-13 10:34:04
     */
    public static List<String> getPackageClassesName(String packagePath, Class<?> aClass, boolean addPath, boolean addClass) {
        List<String> filesPath = getFilesPath(packagePath, aClass);

        List<String> resultList = new ArrayList<>();

        int startIndex = 0;
        for (String className : filesPath) {
            if (addPath) {
                className = className.replace(File.separator, ".");
            } else {
                startIndex = className.lastIndexOf(File.separator) + 1;
            }

            if (addClass) {
                className = className.substring(startIndex);
            } else {
                className = className.substring(startIndex, className.lastIndexOf("."));
            }
            resultList.add(className);
        }
        return resultList;
    }

    /**
     * 已文件路径的形式,返回包下的所有类
     *
     * @param packagePath 包名全路径
     */
    public static List<String> getFilesPath(String packagePath, Class<?> aClass) {
        String filePath;
        if (StringUtil.isNotBlank(packagePath)) {
            filePath = Objects.requireNonNull(aClass.getResource(File.separator)).getPath() + packagePath.replace(".", File.separator);
        } else {
            filePath = Objects.requireNonNull(aClass.getResource("")).getPath();
        }

        List<File> fileList = FileUtil.getFilesByPath(filePath);

        List<String> resultFileList = new ArrayList<>();
        for (File file : fileList) {
            String subFilePath = file.getPath();
            subFilePath = subFilePath.substring(subFilePath.indexOf("classes") + 8);
            if (!subFilePath.contains("$")) { //排除内部类
                resultFileList.add(subFilePath);
            }
        }
        return resultFileList;
    }

    /**
     * 打印系统环境信息
     */
    public static SystemInfoDto printSystemInfo(Class<?> aClass) {
        SystemInfoDto systemInfoDto = new SystemInfoDto();
        systemInfoDto.setOsName(SystemUtils.OS_NAME);
        systemInfoDto.setOsArch(SystemUtils.OS_ARCH);
        systemInfoDto.setOsVersion(SystemUtils.OS_VERSION);
        systemInfoDto.setJavaVmSpecificationVersion(SystemUtils.JAVA_VM_SPECIFICATION_VERSION);
        systemInfoDto.setJavaVmSpecificationVendor(SystemUtils.JAVA_VM_SPECIFICATION_VENDOR);
        systemInfoDto.setJavaVmSpecificationName(SystemUtils.JAVA_VM_SPECIFICATION_NAME);
        systemInfoDto.setJavaVmVersion(SystemUtils.JAVA_VM_VERSION);
        systemInfoDto.setJavaVmName(SystemUtils.JAVA_VM_NAME);
        systemInfoDto.setJavaVmVendor(SystemUtils.JAVA_VM_VENDOR);
        systemInfoDto.setJavaVersion(SystemUtils.JAVA_VERSION);
        systemInfoDto.setProviders(printProviders());
        systemInfoDto.setName(aClass.getName());
        systemInfoDto.setSimpleName(aClass.getSimpleName());
        if (StringUtil.isNotBlank(aClass.getName())) {
            systemInfoDto.setServiceName(Constant.SPRING_APPLICATION_NAME);
            systemInfoDto.setPort(Constant.SERVER_PORT);
            systemInfoDto.setProfilesActive(Constant.SPRING_PROFILES_ACTIVE);
        }

        ProcessHandle current = ProcessHandle.current();
        if (current.info().user().isPresent()) {
            systemInfoDto.setUser(current.info().user().get());
        }
        if (current.info().arguments().isPresent()) {
            systemInfoDto.setArguments(current.info().arguments().get());
        }
        if (current.info().command().isPresent()) {
            systemInfoDto.setCommand(current.info().command().get());
        }
        if (current.info().commandLine().isPresent()) {
            systemInfoDto.setCommandLine(current.info().commandLine().get());
        }
        if (current.info().startInstant().isPresent()) {
            systemInfoDto.setStartInstant(current.info().startInstant().get());
        }
        if (current.info().totalCpuDuration().isPresent()) {
            systemInfoDto.setTotalCpuDuration(current.info().totalCpuDuration().get());
        }
        systemInfoDto.setPid(String.valueOf(ProcessHandle.current().pid()));
        log.info("系统信息(system info):{}", JSONObject.toJSONString(systemInfoDto));
        return systemInfoDto;
    }

    /**
     * 打印jre中算法提供者列表
     */
    public static String printProviders() {
        Provider[] providers = Security.getProviders();
        List<String> poviderName = Arrays.stream(providers).map(Provider::getName).collect(Collectors.toList());
        return StringUtil.join(poviderName, ",");
    }

    public static SystemInfoDto systemInit(ApplicationContext context, Class<?> aClass) {
        return printSystemInfo(aClass);
    }

}
