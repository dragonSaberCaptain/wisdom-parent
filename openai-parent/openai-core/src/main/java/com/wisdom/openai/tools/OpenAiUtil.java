package com.wisdom.openai.tools;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * <p>
 * openai 相关工具类
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/3/22 11:27 星期三
 */
@Slf4j
public class OpenAiUtil {
    private static HashMap<String, String> headerMap;

    public static String sendGetData(String url, String token) {
        String resultData = HttpRequest.get(url).headerMap(initHeaderMap(token), true).execute().body();
        log.info("OpenAiUtil--sendGetData resp:" + resultData);
        return resultData;
    }

    public static String sendPostData(String url, String token, String body) {
        String resultData = HttpRequest.post(url).headerMap(initHeaderMap(token), true).body(body).execute().body();
        log.info("OpenAiUtil--sendPostData resp:" + resultData);
        return resultData;
    }

    public static String sendDeleteData(String url, String token) {
        String resultData = HttpRequest.delete(url).headerMap(initHeaderMap(token), true).execute().body();
        log.info("OpenAiUtil--sendDeleteData resp:" + resultData);
        return resultData;
    }

    public static String sendPostFile(String url, String token, String purpose, File file) {
        String resultData = null;
        if (null != file) {
            resultData = HttpRequest.post(url)
                    .headerMap(initHeaderMap(token), true)
                    .form("purpose", purpose)
                    .form("file", file)
                    .execute().body();
            //删除临时文件
            if (file.exists()) {
                file.delete();
            }
        }
        log.info("OpenAiUtil--sendPostFile resp:" + resultData);
        return resultData;
    }

    public static File dealMultipartFileToFile(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        File tmpFile = null;
        if (StrUtil.isNotEmpty(originalFilename)) {
            tmpFile = new File(originalFilename);
            OutputStream out = null;
            try {
                out = new FileOutputStream(tmpFile);
                byte[] ss = multipartFile.getBytes();
                for (byte s : ss) {
                    out.write(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return tmpFile;
    }

    public static HashMap<String, String> initHeaderMap(String token) {
        if (null == headerMap) {
            headerMap = new HashMap<>();
            headerMap.put("Authorization", "Bearer " + token);
            headerMap.put("Content-Type", "application/json");
        }
        return headerMap;
    }
}
