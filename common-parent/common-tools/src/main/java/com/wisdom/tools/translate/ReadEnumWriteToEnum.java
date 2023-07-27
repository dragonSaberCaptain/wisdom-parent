package com.wisdom.tools.translate;

import com.alibaba.fastjson2.JSON;
import com.wisdom.tools.translate.baidu.TransApi;
import com.wisdom.tools.translate.baidu.TransResult;
import com.wisdom.tools.translate.baidu.TranslateData;

import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 百度翻译工具类 之 翻译并替换枚举类
 *
 * @author captain
 * @version 1.0
 * @datetime 2022/10/28 10:44 星期五
 */
public class ReadEnumWriteToEnum {
    //{"from":"zh","to":"en","trans_result":[{"src":"\u9ad8\u5ea6600\u7c73","dst":"Height 600m"}]}

    public static void main(String[] args) {
        String path = "C:\\Users\\dragonSaberCaptain\\IdeaProjects\\mml3-ccu\\console-parent\\console-access\\src\\main\\java\\com\\jieyi\\util";
        String pathName = "\\AccessPltResult.java";
        System.out.println("正在翻译。。。");
        String pathFull = path + pathName;
        //创建File 对象
        File sourece = new File(pathFull);
        //创建读字符流对象
        BufferedReader br = null;
        //创建文件写入流
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new FileReader(sourece));

            //获取源文件的文件名
            String filename = sourece.getName();
            int indexOf = filename.lastIndexOf(".");
            String fileType = filename.substring(indexOf);
            String name = filename.substring(0, indexOf);
            filename = name + "_test" + fileType;

            File targer = new File(sourece.getParent(), filename);

            bw = new BufferedWriter(new FileWriter(targer));
            int i = 0;
            while (true) {
                i++;
                String str = br.readLine();
                if (str == null) {
                    break;
                }
                Matcher matcher = matcherStr(str);
                String groupStr = null;
                int index = 0;
                while (matcher.find()) {
                    groupStr = matcher.group();
                    index = matcher.start(0);
                }
                if (null != groupStr) {
                    String transResult1 = TransApi.getTransResult(groupStr, "zh", "en");
                    TransResult transResult = JSON.parseObject(transResult1, TransResult.class);
                    List<TranslateData> data = transResult.getTrans_result();
                    if (data.size() > 0) {
                        groupStr = transResult.getTrans_result().get(0).getDst();
                    }
                    str = str.substring(0, index) + groupStr;
                    Thread.sleep(1000);
                }

                //一次读取一行
                str += "\r\n";

                //写入读取的字符串
                bw.write(str);
                //刷新字符流
                bw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭字符流
            try {
                if (bw != null) {
                    bw.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("翻译完毕!!!");
        }
    }

    public static Matcher matcherStr(String str) {
        String regexs = "[(]\\\"(.*?)\\\"\\,( \\\"|\\\")(.*?)\\\"[)](,|;)";
        Pattern pattern = Pattern.compile(regexs);
        return pattern.matcher(str);
    }
}
