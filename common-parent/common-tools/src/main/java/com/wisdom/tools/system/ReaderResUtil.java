package com.wisdom.tools.system;

import com.alibaba.fastjson2.JSON;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * 资源读取工具类 用于获取项目中的配置文件
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/12/2 10:25 星期四
 */
@Data
@Slf4j
@Accessors(chain = true)
public class ReaderResUtil {
    /**
     * 单例
     */
    public static final ReaderResUtil instance = new ReaderResUtil();
    public static boolean isYamlRes;
    private static Map<String, Object> parmsMap = new HashMap<>();

    public void initByPath(String path, String fileType) throws FileNotFoundException {
        initByInputStream(new FileInputStream(path), fileType);
    }

    public void initByInputStream(InputStream inputStream, String fileType) {
        if (parmsMap.size() == 0) {
            try {
                if ("properties".equals(fileType)) {
                    Properties properties = new Properties();
                    properties.load(inputStream);
                    parmsMap = JSON.parseObject(JSON.toJSONString(properties), new TypeToken<Map<String, Object>>() {
                    }.getType());
                    isYamlRes = false;
                } else if ("yaml".equals(fileType) || "yml".equals(fileType)) {
                    Yaml tepYaml = new Yaml();
                    parmsMap = tepYaml.loadAs(inputStream, HashMap.class);
                    isYamlRes = true;
                }
            } catch (Exception e) {
                log.error("read" + fileType + " failed !", e);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * get property
     */
    public Object getValueByKey(String key) {
        String separator = ".";
        String[] separatorKeys = null;
        if (isYamlRes && key.contains(separator)) {
            separatorKeys = key.split("\\.");
        } else {
            return parmsMap.get(key);
        }
        Map<String, Object> finalValue = new HashMap<>();
        for (int i = 0; i < separatorKeys.length - 1; i++) {
            if (i == 0) {
                finalValue = (Map) parmsMap.get(separatorKeys[i]);
                continue;
            }
            if (finalValue == null) {
                break;
            }
            finalValue = (Map) finalValue.get(separatorKeys[i]);
        }
        return finalValue == null ? null : finalValue.get(separatorKeys[separatorKeys.length - 1]);
    }
}
