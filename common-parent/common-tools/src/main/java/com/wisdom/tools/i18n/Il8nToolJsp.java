package com.wisdom.tools.i18n;

import cn.hutool.core.util.StrUtil;
import com.wisdom.tools.translate.baidu.TransApi;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright © 2022 dragonSaberCaptain. All rights reserved.
 * <p>
 * jsp 国际化翻译类
 * <p>
 * 要使用 请到 TransApi.class 下配置百度翻译开发平台 APP_ID
 *
 * @author captain
 * @version 1.0
 * @datetime 2022/12/16 17:09 星期五
 */
public class Il8nToolJsp {

    // 配置中文
    public static final Pattern CHINESE_PATTERN = Pattern.compile("[\\w\\ufe30-\\uffa0\\u4e00-\\u9fa5]*([\\u4e00-\\u9fa5]+[\\P{Punct}-,]{0,1})+[ \\w\\ufe30-\\uffa0\\u4e00-\\u9fa5]*\\P{Punct}{0,1}");
    public static final int BUFF_SIZE = 10240;
    /**
     * 国际化文件中国际化key的前缀
     **/
    private static final String I18N_KEY_PREFIX = "";
    /**
     * 请先配置好以下路径
     */
    // 要国际化的jsp文件存放路径
    private static final String JSP_SOURCE_PATH = "C:\\Users\\dragonSaberCaptain\\IdeaProjects\\wuhuykt\\center-console\\console\\src\\main\\webapp\\webpages\\cqtk\\report";
    // 生成的国际化文件名称，生成的文件保存在当前classpath目录下，重复执行会覆盖之前的执行结果
    private static final String I18N_RES_JSP = "C:\\Users\\dragonSaberCaptain\\IdeaProjects\\wuhuykt\\center-console\\console\\src\\main\\java\\com\\jieyi\\util\\i18n\\i18nResJsp\\";
    // 国际化后生成的资源文件的存储路径
    private static final String JSP_CREATE_PATH = "C:\\Users\\dragonSaberCaptain\\IdeaProjects\\wuhuykt\\center-console\\console\\src\\main\\java\\com\\jieyi\\util\\i18n\\il8nDealResultFile";
    // 已经存在的国际化文件请放在这个目录下，防止对已经国际化的中文再生成重复的国际化文件,这个目录下只存放中文国际化文件即可；
    private static final String EXIST_I18n_ZH_CN_FILE_PATH = "C:\\Users\\dragonSaberCaptain\\IdeaProjects\\wuhuykt\\center-console\\console\\src\\main\\java\\com\\jieyi\\util\\i18n\\i18nSource";
    private static final String PROP_NAME_EN = "message_en_US.properties";
    /**
     * 请配置要生成的国际化文件的文件名
     */
    private static final String PROP_NAME_ZH = "message_zh_CN.properties";
    // 文件编码utf-8
    private static final String ENCODING = "utf-8";
    private static final String PROP_NAME_PT = "message_pt_PT.properties";
    // html注释<!-- XXXX -->
    private static final String regEx_html = "<!--.*?-->";
    private static final Pattern p_html = Pattern.compile(regEx_html, Pattern.DOTALL);
    // jsp的注释<%-- XXXXX --%>
    private static final String regEx_jsp = "<%--.*?--%>";
    private static final Pattern p_jsp = Pattern.compile(regEx_jsp, Pattern.DOTALL);
    // js注释/* XXXX */
    private static final String regEx_js1 = "/\\*.*?\\*/";
    private static final Pattern p_js1 = Pattern.compile(regEx_js1, Pattern.DOTALL);
    // js注释 // XXXXX
    private static final String regEx_js2 = "//[\\s\\S]*?\n";
    private static final Pattern p_js2 = Pattern.compile(regEx_js2, Pattern.CASE_INSENSITIVE);

    // jsp中的java代码 ${XXXX}
    private static final String regEx_java = "\\$\\{.*?\\}";
    private static final Pattern p_java = Pattern.compile(regEx_java, Pattern.DOTALL);
    // jsp代码<% XXXX %>
    private static final String regEx_jsp2 = "<%.*?%>";
    private static final Pattern p_jsp2 = Pattern.compile(regEx_jsp2, Pattern.DOTALL);
    // 不匹配的中文缓存
    private static final Map<String, String> noReplace = new HashMap<>();
    // 被匹配的中文内容 key为匹配到的中文 ，value为Il8NBean
    private static final List<I18NBean> i18nMatchCache = new ArrayList<>();
    private final static String IL8N_TAGLIB_FMT = "<%@ taglib prefix=\"fmt\" uri=\"http://java.sun.com/jsp/jstl/fmt\" %>\r\n";
    private final static String IL8N_TAGLIB_FMT_CHECK = "uri=\"http://java.sun.com/jsp/jstl/fmt\"";
    private final static String IL8N_TAGLIB_CHECK = "file=\"../include/taglibs.jsp\"%>";
    private final static String IL8N_TAGLIB_SPRING = "<%@ taglib prefix=\"spring\" uri=\"http://www.springframework.org/tags\" %>\r\n";
    private final static String IL8N_TAGLIB_SPRING_CHECK = "uri=\"http://www.springframework.org/tags\"";
    // 已经国际化的kv
    private static Map<String, String> existMap;
    // 需要被国际化的jsp文件列表
    private static List<File> fileList = new ArrayList<>();
    //缓存翻译结果map
    private static Map<String, String> cacheMap = new HashMap<>();

    public static void main(String[] args) throws Exception {
        // 初始化
        init();
        // 获取要国际化的中文，并在当前classpath下生成properties文件；
        getChinereWord();
        // 生成将中文替换为国际化key的jsp文件；
        replace(JSP_SOURCE_PATH, JSP_CREATE_PATH);
    }

    /**
     * 初始化
     *
     * @author captain
     * @datetime 2022-12-19 13:45:35
     */
    private static void init() {
        // 不做国际化的内容，例如html页面上css中的字体
        noReplace.put("微软雅黑", "");
        noReplace.put("宋体", "");
        // 缓存已经做过国际化的key和value；
        existMap = loadExistI18nFiles();
        // 获取需要被国际化的jsp文件列表
        fileList = getJsps(new File(JSP_SOURCE_PATH));
    }

    /**
     * 获取到当前已经国际化的key和value
     *
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author captain
     * @datetime 2022-12-19 13:45:18
     */
    private static Map<String, String> loadExistI18nFiles() {
        File baseDir = new File(EXIST_I18n_ZH_CN_FILE_PATH);
        if (!baseDir.exists()) {
            return new HashMap<>(0);
        }
        File[] files = baseDir.listFiles();
        Map<String, String> existI18n = new HashMap<>(3000);
        for (File file : files)
            if (file.isDirectory() && !file.isHidden()) continue;
            else if (file.getName().toLowerCase().endsWith(".properties")) existI18n.putAll(loadProperties(file));
        return existI18n;
    }


    /**
     * 载入已经存在的国际化文件
     *
     * @param file 文件
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author captain
     * @datetime 2022-12-19 13:44:22
     */
    private static Map<String, String> loadProperties(File file) {
        Map<String, String> map = new HashMap<>();
        Properties prop = new Properties();
        InputStream in;
        try {
            in = new FileInputStream(file);
            prop.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Map.Entry<Object, Object> e : prop.entrySet()) {
            map.put(String.valueOf(e.getKey()), String.valueOf(e.getValue()));
        }
        return map;
    }

    /**
     * 替换字符
     *
     * @param sourcePath 源地址
     * @param targetPath 目标地址
     * @author captain
     * @datetime 2022-12-19 13:46:09
     */
    private static void replace(String sourcePath, String targetPath) {
        System.out.println("共有文件：" + fileList.size());
        for (File file : fileList) {
            // 查找当前要替换的字符
            String content = file2String(file, "utf-8");
            content = p_html.matcher(content).replaceAll("\n");
            content = p_jsp2.matcher(content).replaceAll("\n");
            content = p_js1.matcher(content).replaceAll("\n");
            content = p_js2.matcher(content).replaceAll("\n");
            content = p_java.matcher(content).replaceAll("\n");
            // 获取国际化后的文件内容
            content = getI18nContent(file, content);
            // 生成国际化文件
            File newFile = new File(file.getAbsolutePath().replace(sourcePath, targetPath));
            File dir = new File(newFile.getPath().substring(0, newFile.getPath().lastIndexOf("\\")));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            string2File(content, newFile);
        }
    }

    /**
     * 获取文件国际化后的内容
     *
     * @param file    文件
     * @param content 内容
     * @author captain
     * @datetime 2022-12-19 13:46:47
     */
    private static String getI18nContent(File file, String content) {
        // System.out.println(content);
        /** 本页面匹配到的中文 */
        Matcher ma = CHINESE_PATTERN.matcher(content);
        // 缓存当前页面需要被匹配中文和生成的key Map<中文，匹配到的key>
        Map<String, String> selfMap = new HashMap<>();
        //用来标记是否有匹配的中文
        boolean isMatch = false;
        while (ma.find()) {
            String pipei = ma.group().trim();
            // 忽略需要被排除的中文
            if (noReplace.get(pipei) != null) {
                continue;
            }
            String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
            Optional<I18NBean> iL8NBeanOpt = i18nMatchCache.stream().filter(i18NBeanSub -> pipei.equals(i18NBeanSub.getChinese()) && fileName.equals(i18NBeanSub.getFileName())).findFirst();
            String key = null;
            try {
                key = iL8NBeanOpt.get().getKey();
            } catch (Exception e) {
                System.out.println("fileName:" + fileName + " pipei:" + pipei);
            }
            if (key != null) selfMap.put(pipei, key);
            isMatch = true;
        }
        /** 开始替换 **/
        content = file2String(file, "utf-8");
        StringBuffer sb = new StringBuffer();
        ma = CHINESE_PATTERN.matcher(content);
        while (ma.find()) {
            if (noReplace.get(ma.group().trim()) != null) {
                continue;
            }
            String key = selfMap.get(ma.group().trim());
            //替换
            ma.appendReplacement(sb, key == null ? ma.group() : getI18nToSpring(key));
        }
        ma.appendTail(sb);
        /** 判断是否已经添加国际化标签，没有则添加 */
        //if (isMatch && sb.indexOf(IL8N_TAGLIB_CHECK) < 0 && sb.indexOf(IL8N_TAGLIB_SPRING_CHECK) < 0) {
        //    return new StringBuilder(IL8N_TAGLIB_SPRING).append(sb).toString();
        //}
        return sb.toString();
    }

    /**
     * 返回国际化标签 fmt标签
     * <fmt:message key=value"/>
     *
     * @param str 字符串
     * @return java.lang.String
     * @author captain
     * @datetime 2022-12-19 13:47:26
     */
    private static String getI18nToFmt(String str) {
        return "<fmt:message key=\"" + str + "\"/>";
    }

    /**
     * 返回国际化标签 spring标签
     * <spring:message key=value"/>
     *
     * @param str 字符串
     * @return java.lang.String
     * @author captain
     * @datetime 2022-12-19 13:47:26
     */
    private static String getI18nToSpring(String str) {
        return "<spring:message code=\"" + str + "\"/>";
    }

    /**
     * 扫描jsp，提取中文，并翻译，生成国际化资源文件
     *
     * @author captain
     * @datetime 2022-12-19 10:37:09
     */
    private static void getChinereWord() throws Exception {
        for (File file : fileList) {
            String content = file2String(file, "utf-8");
            // 预处理
            // 去掉html注释 <!--.*?-->
            content = p_html.matcher(content).replaceAll("\n");
            // 去掉jsp注释 <%--.*?--%>
            content = p_jsp.matcher(content).replaceAll("\n");
            // 去掉jsp注释2 /* */
            content = p_js1.matcher(content).replaceAll("\n");
            // 去掉jsp注释3 //
            content = p_js2.matcher(content).replaceAll("\n");
            content = p_java.matcher(content).replaceAll("\n");

            // 匹配中文
            Matcher ma = CHINESE_PATTERN.matcher(content);
            String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
            while (ma.find()) {
                // 获取到匹配的中文
                String pipei = ma.group().trim();
                // 过滤掉不需要匹配的中文内容
                if (noReplace.get(ma.group().trim()) != null) {
                    continue;
                }
                I18NBean il8 = new I18NBean();

                il8.setFileName(fileName);
                il8.setChinese(pipei);
                il8.setFile(file.getAbsolutePath().replace(JSP_SOURCE_PATH, ""));

                StringBuilder sb = new StringBuilder();
                if (StrUtil.isNotEmpty(I18N_KEY_PREFIX)) {
                    sb.append(I18N_KEY_PREFIX).append(".");
                }
                sb.append(fileName).append(".");
                sb.append(ChineseToPinyin.getFieldName(pipei, true));
                il8.setKey(sb.toString());

                Optional<I18NBean> iL8NBeanOpt = i18nMatchCache.stream().filter(i18NBeanSub -> il8.getKey().equals(i18NBeanSub.getKey())).findFirst();
                if (iL8NBeanOpt.isPresent()) {
                    sb = new StringBuilder();
                    if (StrUtil.isNotEmpty(I18N_KEY_PREFIX)) {
                        sb.append(I18N_KEY_PREFIX).append(".");
                    }
                    sb.append(fileName).append(".");
                    sb.append(ChineseToPinyin.getFieldName(pipei, false));
                    il8.setKey(sb.toString());
                }

                iL8NBeanOpt = i18nMatchCache.stream().filter(i18NBeanSub -> pipei.equals(i18NBeanSub.getChinese()) && fileName.equals(i18NBeanSub.getFileName())).findFirst();
                if (!iL8NBeanOpt.isPresent()) {
                    i18nMatchCache.add(il8);
                }
            }
        }
        System.out.println("翻译总大小:" + i18nMatchCache.size());
        // 翻译
        translate(i18nMatchCache);
        // 打印出全部的中文和生成的key；
        for (I18NBean e : i18nMatchCache) {
            System.out.println(e.getChinese() + ":::" + e.getKey());
            System.out.println(e.getEnglish() + ":::" + e.getKey());
            System.out.println(e.getPortugal() + ":::" + e.getKey());
        }
        //升序排序
        i18nMatchCache.sort(Comparator.comparing(I18NBean::getKey));
        // 生成i18n文件
        createIL8NFile(i18nMatchCache);
    }

    /**
     * 直接调用百度翻译 用这个方法
     *
     * @param i18NBeanList 翻译数据源
     * @author captain
     * @datetime 2022-12-19 13:36:15
     */
    private static void translate(List<I18NBean> i18NBeanList) throws Exception {
        for (I18NBean i18NBean : i18NBeanList) {
            String value = TransApi.transDealZhTOEn(i18NBean.getChinese());
            i18NBean.setEnglish(value);
        }
    }

    /**
     * 生成国际化文件
     *
     * @param i18NBeanList 数据源
     * @author captain
     * @datetime 2022-12-19 13:43:31
     */
    private static void createIL8NFile(List<I18NBean> i18NBeanList) {
        Writer osw_en = null, osw_zh = null, osw_pt = null;

        Set<String> tmpSet = new HashSet<>();
        try {
            osw_en = new OutputStreamWriter(new FileOutputStream(I18N_RES_JSP + PROP_NAME_EN), ENCODING);
            osw_zh = new OutputStreamWriter(new FileOutputStream(I18N_RES_JSP + PROP_NAME_ZH), ENCODING);
            osw_pt = new OutputStreamWriter(new FileOutputStream(I18N_RES_JSP + PROP_NAME_PT), ENCODING);

            int existCount = 0;
            for (I18NBean i18NBean : i18NBeanList) {
                // 已经被国际化的key就不再处理了；
                if (existMap.get(i18NBean.getKey()) != null) {
                    existCount++;
                    continue;
                }
                if (tmpSet.add(i18NBean.getFileName())) {
                    osw_zh.write("\n");
                    osw_zh.write("#" + i18NBean.getFiles().toString() + "\n");
                    osw_en.write("\n");
                    osw_en.write("#" + i18NBean.getFiles().toString() + "\n");
                    osw_pt.write("\n");
                    osw_pt.write("#" + i18NBean.getFiles().toString() + "\n");
                }
                osw_zh.write(i18NBean.getKey() + "=" + i18NBean.getChinese() + "\n");
                osw_en.write(i18NBean.getKey() + "=" + i18NBean.getEnglish() + "\n");
                osw_pt.write(i18NBean.getKey() + "=" + i18NBean.getPortugal() + "\n");
            }

            System.out.println("\n\r\n\r生成国际化资源文件");
            System.out.println(I18N_RES_JSP + PROP_NAME_ZH);
            System.out.println(I18N_RES_JSP + PROP_NAME_EN);
            System.out.println(I18N_RES_JSP + PROP_NAME_PT);
            System.out.println("共有" + i18NBeanList.size() + "个key，" + existCount + "个key已经存在," + (i18NBeanList.size() - existCount) + "个key已写入国际化文件");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (osw_en != null) {
                try {
                    osw_en.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (osw_zh != null) {
                try {
                    osw_zh.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (osw_pt != null) {
                try {
                    osw_pt.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将文本写入到文件
     *
     * @param content 内容
     * @param file    文件
     * @author captain
     * @datetime 2022-12-19 13:48:28
     */
    public static void string2File(String content, File file) {
        try {
            Writer osw = new OutputStreamWriter(new FileOutputStream(file), ENCODING);
            osw.write(content);
            osw.close();
            System.out.println("生成国际化文件：" + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 遍历path目录，获取全部的jsp文件，并将其放入到fileList列表里面；
     *
     * @param path 文件
     * @return java.util.List<java.io.File>
     * @author captain
     * @datetime 2022-12-19 13:48:54
     */
    private static List<File> getJsps(File path) {
        return getFiles(path, ".jsp");
    }

    private static List<File> getJs(File path) {
        return getFiles(path, ".js");
    }

    /**
     * 遍历path目录，获取全部的匹配文件，并将其放入到fileList列表里面；
     *
     * @param path
     * @return java.util.List<java.io.File>
     * @author captain
     * @datetime 2022-12-28 14:30:11
     */
    public static List<File> getFiles(File path, String fileType) {
        List<File> fileList = new ArrayList<>();
        File[] files = path.listFiles();
        for (File file : files)
            if (file.isDirectory() && !file.isHidden()) {
                fileList.addAll(getFiles(file, fileType));
            } else if (file.getName().toLowerCase().endsWith(fileType)) {
                fileList.add(file);
                System.out.println("需国际化的" + fileType + "文件:" + file.getAbsolutePath());
            }
        return fileList;
    }

    public static File getFilesByAbsolutePath(String absolutePath) {
        return new File(absolutePath);
    }

    public static List<File> getFilesByRelativePath(String relativePath, String fileType) {
        return getFiles(new File(relativePath), fileType);
    }

    /**
     * 读取文件内容，返回字符串；
     *
     * @param file     文件
     * @param encoding 编码
     * @return java.lang.String
     * @author captain
     * @datetime 2022-12-19 13:49:22
     */
    public static String file2String(File file, String encoding) {
        StringWriter writer = new StringWriter();
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file), encoding);
            char[] buffer = new char[BUFF_SIZE];
            int n;
            while (-1 != (n = reader.read(buffer))) {
                writer.write(buffer, 0, n);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer.toString();
    }
}