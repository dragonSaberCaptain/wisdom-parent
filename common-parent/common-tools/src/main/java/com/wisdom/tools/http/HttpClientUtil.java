package com.wisdom.tools.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Copyright © 2017 captain. All rights reserved.
 *
 * @Description: HttpClient
 * @author: captain
 * @date: 2017年8月28日 上午9:08:24
 */
@Slf4j
public class HttpClientUtil {
    private final static int CONNECT_TIMEOUT = 8000;// 连接超时毫秒
    private final static int SOCKET_TIMEOUT = 20000;// 传输超时毫秒
    private final static int REQUESTCONNECT_TIMEOUT = 5000;// 获取请求超时毫秒
    private final static int CONNECT_TOTAL = 500;// 最大连接数
    private final static int CONNECT_ROUTE = 20;// 每个路由基础的连接数
    private final static String ENCODE_CHARSET = "utf-8";// 响应报文解码字符集
    private final static String RESP_CONTENT = "通信失败";
    private static PoolingHttpClientConnectionManager connManager;
    private static CloseableHttpClient httpClient;

    static {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory
                .getSocketFactory();
        LayeredConnectionSocketFactory sslsf = createSSLConnSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder
                .<ConnectionSocketFactory>create().register("http", plainsf)
                .register("https", sslsf).build();

        connManager = new PoolingHttpClientConnectionManager(registry);
        // 将最大连接数增加到200
        connManager.setMaxTotal(CONNECT_TOTAL);
        // 将每个路由基础的连接增加到20
        connManager.setDefaultMaxPerRoute(CONNECT_ROUTE);
        // 可用空闲连接过期时间,重用空闲连接时会先检查是否空闲时间超过这个时间，如果超过，释放socket重新建立
        connManager.setValidateAfterInactivity(30000);
        // 设置socket超时时间
        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(SOCKET_TIMEOUT).build();
        connManager.setDefaultSocketConfig(socketConfig);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(REQUESTCONNECT_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT).build();
        HttpRequestRetryHandler httpRequestRetryHandler = (exception, executionCount, context) -> {
            // 如果已经重试了5次，就放弃
            if (executionCount >= 5) {
                log.info("重试");
                return false;
            }
            // 如果服务器丢掉了连接，那么就重试
            if (exception instanceof NoHttpResponseException) {
                log.info("服务器丢掉了连接，那么就重试");
                return true;
            }
            // 不要重试SSL握手异常
            if (exception instanceof SSLHandshakeException) {
                log.info("不要重试SSL握手异常");
                return false;
            }
            // 超时
            if (exception instanceof InterruptedIOException) {
                log.info("超时");
                return true;
            }
            // 目标服务器不可达
            if (exception instanceof UnknownHostException) {
                log.info("目标服务器不可达");
                return false;
            }
            // 连接被拒绝
//            if (exception instanceof ConnectTimeoutException) {
//                log.info("连接被拒绝");
//                return false;
//            }
            // ssl握手异常
            if (exception instanceof SSLException) {
                log.info("ssl握手异常");
                return false;
            }
            HttpClientContext clientContext = HttpClientContext
                    .adapt(context);
            HttpRequest request = clientContext.getRequest();
            // 如果请求是幂等的，就再次尝试
            if (!(request instanceof HttpEntityEnclosingRequest)) {
                log.info("幂等请求,再次尝试");
                return true;
            }
            return false;
        };
        httpClient = HttpClients.custom().setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(httpRequestRetryHandler).build();
        if (connManager != null && connManager.getTotalStats() != null) {
            log.info("now client pool "
                    + connManager.getTotalStats().toString());
        }
    }

    /**
     * 发送HTTP_GET请求
     *
     * @param reqURL 请求地址(含参数)
     * @return 远程主机响应正文
     * @see 1)该方法会自动关闭连接,释放资源
     * @see 2)方法内设置了连接和读取超时时间,单位为毫秒,超时或发生其它异常时方法会自动返回"通信失败"字符串
     * @see 3)请求参数含中文时,经测试可直接传入中文,HttpClient会自动编码发给Server,应用时应根据实际效果决 定传入前是否转码
     * @see 4)该方法会自动获取到响应消息头中[Content-Type:text/html;
     * charset=GBK]的charset值作为响应报文的 解码字符集
     * @see 5)若响应消息头中无Content-Type属性,则会使用HttpClient内部默认的ISO-8859-1作为响应报文的解码字符 集
     */
    public static String sendGetRequest(String reqURL, String param) {
        if (null != param) {
            reqURL += "?" + param;
        }
        // 响应内容
        String respContent = RESP_CONTENT;
        HttpGet httpget = new HttpGet(reqURL);
        CloseableHttpResponse response = null;
        try {
            // 执行GET请求
            response = httpClient.execute(httpget, HttpClientContext.create());
            // 获取响应实体
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                Charset respCharset = ContentType.getOrDefault(entity)
                        .getCharset();
                respContent = EntityUtils.toString(entity, respCharset);
                EntityUtils.consume(entity);
            }
        } catch (ConnectTimeoutException cte) {
            log.error("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下", cte);
        } catch (SocketTimeoutException ste) {
            log.error("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下", ste);
        } catch (ClientProtocolException cpe) {
            // 该异常通常是协议错误导致:比如构造HttpGet对象时传入协议不对(将'http'写成'htp')or响应内容不符合HTTP协议要求等
            log.error("请求通信[" + reqURL + "]时协议异常,堆栈轨迹如下", cpe);
        } catch (ParseException pe) {
            log.error("请求通信[" + reqURL + "]时解析异常,堆栈轨迹如下", pe);
        } catch (IOException ioe) {
            // 该异常通常是网络原因引起的,如HTTP服务器未启动等
            log.error("请求通信[" + reqURL + "]时网络异常,堆栈轨迹如下", ioe);
        } catch (Exception e) {
            log.error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpget.releaseConnection();
        }
        return respContent;
    }

    public static String sendPostRequest(String reqURL, String param) {
        return sendPostRequest(reqURL, param, null, "utf-8");
    }

    public static String sendPostRequest(String reqURL, String param, Map<String, Object> addHeaderMap) {
        return sendPostRequest(reqURL, param, addHeaderMap, "utf-8");
    }

    /**
     * 发送HTTP_POST请求 type: 默认是表单请求，
     *
     * @param reqURL 请求地址
     * @param param  请求参数,若有多个参数则应拼接为param11=value11&22=value22&33=value33的形式
     * @param type   编码字符集,编码请求数据时用之,此参数为必填项(不能为""或null)
     * @return 远程主机响应正文
     * @see 1)该方法允许自定义任何格式和内容的HTTP请求报文体
     * @see 2)该方法会自动关闭连接,释放资源
     * @see 3)方法内设置了连接和读取超时时间,单位为毫秒,超时或发生其它异常时方法会自动返回"通信失败"字符串
     * @see 4)请求参数含中文等特殊字符时,可直接传入本方法,并指明其编码字符集encodeCharset参数,方法内部会自 动对其转码
     * @see 5)该方法在解码响应报文时所采用的编码,取自响应消息头中的[Content-Type:text/html; charset=GBK]的
     * charset值
     * @see 6)若响应消息头中未指定Content-Type属性,则会使用HttpClient内部默认的ISO-8859-1
     */
    public static String sendPostRequest(String reqURL, String param, Map<String, Object> addHeaderMap,
                                         String type) {
        String result = RESP_CONTENT;
        // 设置请求和传输超时时间
        HttpPost httpPost = new HttpPost(reqURL);
        // 这就有可能会导致服务端接收不到POST过去的参数,比如运行在Tomcat6.0.36中的Servlet,所以我们手工指定CONTENT_TYPE头消息
        if (type != null && type.length() > 0) {
            httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json; charset=" + ENCODE_CHARSET);
        } else {
            httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=" + ENCODE_CHARSET);
        }
        if (addHeaderMap != null) {
            for (Map.Entry<String, Object> entry : addHeaderMap.entrySet()) {
                httpPost.addHeader(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }

        CloseableHttpResponse response = null;
        try {
            if (param != null) {
                StringEntity entity = new StringEntity(param, ENCODE_CHARSET);
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }
            log.info("开始执行请求：" + reqURL);
            response = httpClient.execute(httpPost, HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                result = EntityUtils.toString(entity,
                        ContentType.getOrDefault(entity).getCharset());
                log.info("执行请求完毕：" + result);
                EntityUtils.consume(entity);
            }
        } catch (ConnectTimeoutException cte) {
            log.error("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下", cte);
        } catch (SocketTimeoutException ste) {
            log.error("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下", ste);
        } catch (ClientProtocolException cpe) {
            log.error("请求通信[" + reqURL + "]时协议异常,堆栈轨迹如下", cpe);
        } catch (ParseException pe) {
            log.error("请求通信[" + reqURL + "]时解析异常,堆栈轨迹如下", pe);
        } catch (IOException ioe) {
            log.error("请求通信[" + reqURL + "]时网络异常,堆栈轨迹如下", ioe);
        } catch (Exception e) {
            log.error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpPost.releaseConnection();
        }
        return result;
    }

    // SSL的socket工厂创建
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;

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

            SSLContext sslContext = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
            sslContext.init(null, new TrustManager[]{trustManager}, new java.security.SecureRandom());
            // 创建SSLSocketFactory , // 不校验域名 ,取代以前验证规则
            sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sslsf;
    }

    public static Map<HttpRoute, PoolStats> getConnManagerStats() {
        if (connManager != null) {
            Set<HttpRoute> routeSet = connManager.getRoutes();
            if (routeSet != null && !routeSet.isEmpty()) {
                Map<HttpRoute, PoolStats> routeStatsMap = new HashMap<>(16);
                for (HttpRoute route : routeSet) {
                    PoolStats stats = connManager.getStats(route);
                    routeStatsMap.put(route, stats);
                }
                return routeStatsMap;
            }
        }
        return null;
    }

    public static PoolStats getConnManagerTotalStats() {
        if (connManager != null) {
            return connManager.getTotalStats();
        }
        return null;
    }

    /**
     * 关闭系统时关闭httpClient
     */
    public static void releaseHttpClient() {
        try {
            httpClient.close();
        } catch (IOException e) {
            log.error("关闭httpClient异常" + e);
        } finally {
            if (connManager != null) {
                connManager.shutdown();
            }
        }
    }
}