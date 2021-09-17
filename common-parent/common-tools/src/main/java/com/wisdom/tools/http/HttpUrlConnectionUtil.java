package com.wisdom.tools.http;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @version 1.0
 * @apiNote java自带的标准类HttpURLConnection去实现
 * @date 2021/6/28 10:05 星期一
 */
public class HttpUrlConnectionUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUrlConnectionUtil.class);

    private static final ScheduledExecutorService scheduledExecutorService;

    static {
        //初始化线程池
        scheduledExecutorService = new ScheduledThreadPoolExecutor(25,
                new BasicThreadFactory.Builder().namingPattern("HttpUrlConnection-schedule-pool-%d").daemon(true).build());
    }

    /**
     * 同步请求
     *
     * @param url           请求路径
     * @param method        请求方法
     * @param jsonStrParams 请求参数
     * @author captain
     * @datetime 2021-08-23 10:52:58
     */
    public static String sendHttpSyn(String url, String method, String jsonStrParams) {
        HttpURLConnection urlConnection = null;

        if (url.startsWith("https://")) {
            HttpsURLConnection.setDefaultSSLSocketFactory(createSSLSocketFactory());
        }

        OutputStream outputStream = null;
        BufferedReader br = null;
        try {
            URL sendUrl = new URL(url);
            urlConnection = (HttpURLConnection) sendUrl.openConnection();
            // 设置请求方式
            urlConnection.setRequestMethod(method);
            // 设置连接超时
            urlConnection.setConnectTimeout(60000);
            // 设置读取超时
            urlConnection.setReadTimeout(60000);

            setRequestProperty(urlConnection);

            // 只有当POST请求时才会执行此代码段
            if (jsonStrParams != null && "POST".equalsIgnoreCase(urlConnection.getRequestMethod())) {
                // 设置输入可用
                urlConnection.setDoInput(true);
                // 设置输出可用
                urlConnection.setDoOutput(true);
                // 设置缓存不可用
                urlConnection.setUseCaches(false);

                byte[] bytes = jsonStrParams.getBytes();
                // 设置文件长度
                urlConnection.setRequestProperty("Content-Length", String.valueOf(bytes.length));
                outputStream = urlConnection.getOutputStream();
                outputStream.write(bytes);
                outputStream.flush();
            }
            if (200 == urlConnection.getResponseCode()) {
                InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8);
                br = new BufferedReader(inputStreamReader);
                StringBuilder sbf = new StringBuilder();
                String readStr;
                while ((readStr = br.readLine()) != null) {
                    sbf.append(readStr);
                }
                return sbf.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("IO异常");
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    /**
     * 异步请求
     *
     * @param url           请求路径
     * @param method        请求方法
     * @param jsonStrParams 请求参数
     * @author captain
     * @datetime 2021-08-23 10:52:58
     */
    public static String sendHttpAsyn(String url, String method, String jsonStrParams) {
        Future future = scheduledExecutorService.submit(() ->
                sendHttpSyn(url, method, jsonStrParams)
        );
        String str = null;
        try {
            str = String.valueOf(future.get());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String sendGetSyn(String url, String jsonStrParams) {
        return sendHttpSyn(url, "GET", jsonStrParams);
    }

    public static String sendGetASyn(String url, String jsonStrParams) {
        return sendHttpAsyn(url, "GET", jsonStrParams);
    }

    public static String sendPostSyn(String url, String jsonStrParams) {
        return sendHttpSyn(url, "POST", jsonStrParams);
    }

    public static String sendPostASyn(String url, String jsonStrParams) {
        return sendHttpAsyn(url, "POST", jsonStrParams);
    }

    /**
     * 设置请求头参数
     *
     * @param httpUrlConnection 连接
     * @author captain
     * @datetime 2021-08-23 10:48:27
     */
    private static void setRequestProperty(HttpURLConnection httpUrlConnection) {
        //设置接受的内容类型
        httpUrlConnection.setRequestProperty("Accept", "*/*");
        //Accept-Language 设置接受的语言 q是权重系数
        httpUrlConnection.setRequestProperty("Accept-Language", "Accept-Language: zh-CN,zh;q=0.9");
        //设置长连接 close 关闭
        httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");

        /**
         * application/json： JSON数据格式
         * application/octet-stream ： 二进制流数据（如常见的文件下载）
         * application/x-www-form-urlencoded ： <form encType=””>中默认的encType，form表单数据被编码为key/value格式发送到服务器（表单默认的提交数据的格式）
         * multipart/form-data ： 需要在表单中进行文件上传时，就需要使用该格式
         * */
        httpUrlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
    }

    // SSL的socket工厂创建
    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory sslsf = null;

        // 创建TrustManager() 用于解决javax.net.ssl.SSLPeerUnverifiedException: peer not authenticated
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                // 检查客户端证书
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                // 检查服务器端证书
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                // 返回受信任的X509证书数组getInstance
                return new X509Certificate[0];
            }
        };

        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
            sslsf = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslsf;
    }
}
