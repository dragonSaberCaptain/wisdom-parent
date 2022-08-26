package com.wisdom.tools.system;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * 资源读取工具类
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/12/2 10:25 星期四
 */
@Data
@Slf4j
@Accessors(chain = true)
public class ReaderResUtil {
	private static Map<String, Object> parmsMap = new HashMap<>();

	/**
	 * 单例
	 */
	public static final ReaderResUtil instance = new ReaderResUtil();

	public static boolean isYamlRes;

//	public void initYaml(Class<?> clazz, String resName) {
//		if (parmsMap.size() == 0) {
//			Yaml tepYaml = new Yaml();
//			try {
//				InputStream inputStream = clazz.getResourceAsStream(resName);
//				parmsMap = tepYaml.loadAs(inputStream, HashMap.class);
//				inputStream.close();
//			} catch (Exception e) {
//				log.error("Init yaml failed !", e);
//			}
//		}
//	}

	public void init(Class<?> clazz, String resName) {
		if (parmsMap.size() == 0) {
			InputStream inputStream = null;
			try {
				String resType = resName.substring(resName.lastIndexOf(".") + 1);
				inputStream = clazz.getResourceAsStream(resName);
				if ("properties".equals(resType)) {
					Properties properties = new Properties();
					properties.load(inputStream);
					parmsMap = JSONObject.parseObject(JSONObject.toJSONString(properties), new TypeReference<>() {
					});
					isYamlRes = false;
				} else if ("yaml".equals(resType) || "yml".equals(resType)) {
					Yaml tepYaml = new Yaml();
					parmsMap = tepYaml.loadAs(inputStream, HashMap.class);
					isYamlRes = true;
				}
			} catch (Exception e) {
				log.error("init " + resName + " failed !", e);
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

//	public void initProperties(Class<?> clzz) {
//		if (properties.size() == 0) {
//			try {
//				URL url = clzz.getProtectionDomain().getCodeSource().getLocation();
//				int lastX = url.toURI().getPath().lastIndexOf("/");
//				String path = url.toURI().getPath().substring(0, lastX);
//				InputStream inputStream = new FileInputStream(path + File.separator + "application.yaml");
//				Yaml tepYaml = new Yaml();
//				properties = tepYaml.loadAs(inputStream, HashMap.class);
//			} catch (Exception e) {
//				log.error("Init yaml failed !", e);
//			}
//		}
//	}

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
