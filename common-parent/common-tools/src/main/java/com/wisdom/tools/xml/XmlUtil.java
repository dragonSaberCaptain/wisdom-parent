/*
 * Copyright (c) 2023 dragonSaberCaptain.All rights reserved.
 * 当前项目名:wisdom-parent
 * 当前模块名:common-tools
 * 当前文件的权限定名:com.wisdom.tools.xml.XmlUtil
 * 当前文件的名称:XmlUtil.java
 * 当前文件的类名:XmlUtil
 * 上一次文件修改的日期时间:2023/7/31 下午6:03
 *
 */

package com.wisdom.tools.xml;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

/**
 * xml转对象,对象转xml
 * 需要配合注解:
 * 在类上加:
 * XmlRootElement(name = "TX_INFO")
 * XmlAccessorType(XmlAccessType.FIELD)
 * 在字段上加:
 * (行)XmlElement
 * (列)XmlAttribute
 *
 * @author captain
 * @version 1.0
 * @projectName jieyi-saas-parent
 * @packageName com.jieyi.util.ccbecny
 * @dateTime 2023/7/31 17:39 星期一
 */
@Slf4j
@Data
@Accessors(chain = true)
public class XmlUtil {
    /**
     * 对象转xml,java对象转换为XML字符串
     *
     * @param obj    Java对象
     * @param load   Java对象类
     * @param coding 编码
     * @return String
     * @author captain
     * @datetime 2023-07-31 17:34:43
     */
    public static String beanToXml(Object obj, Class<?> load, String coding) {
        StringWriter writer = null;
        try {
            JAXBContext context = JAXBContext.newInstance(load);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, coding);
            writer = new StringWriter();
            marshaller.marshal(obj, writer);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return writer != null ? writer.toString() : null;
    }

    /**
     * 对象转xml,java对象转换为XML字符串,并上传到指定路径
     *
     * @param obj     Java对象
     * @param load    Java对象类
     * @param coding  编码
     * @param xmlPath 文件存放地址
     * @author captain
     * @datetime 2023-07-31 17:36:05
     */
    public static void beanToXmlAndSaveFile(Object obj, Class<?> load, String coding, String xmlPath) {
        String xmlString = beanToXml(obj, load, coding);
        if (xmlString != null) {
            //写入到xml文件中
            BufferedWriter bfw = null;
            try {
                bfw = new BufferedWriter(new FileWriter(xmlPath));
                bfw.write(xmlString);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bfw != null) {
                        bfw.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * xml转对象,从字符串转换为Java对象
     *
     * @param xmlSrc xml源
     * @param clazz  实例化类
     */
    public static <T> T xmlStrToBean(String xmlSrc, Class<T> clazz) {
        return xmlStrReadToBean(new StringReader(xmlSrc), clazz);
    }

    /**
     * xml转对象,从StringReader转换为Java对象
     *
     * @param xmlSrc xml源
     * @param clazz  实例化类
     */
    public static <T> T xmlStrReadToBean(StringReader xmlSrc, Class<T> clazz) {
        Object object = null;
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            object = unmarshaller.unmarshal(xmlSrc);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return (T) object;
    }

    /**
     * xml转对象,从文件地址转换为Java对象
     *
     * @param xmlSrc xml源
     * @param clazz  实例化类
     */
    public static <T> T xmlFilePathToBean(String xmlSrc, Class<T> clazz) {
        return xmlFileToBean(new File(xmlSrc), clazz);
    }

    /**
     * xml转对象,从文件转换为Java对象
     *
     * @param xmlSrc xml源
     * @param clazz  实例化类
     */
    public static <T> T xmlFileToBean(File xmlSrc, Class<T> clazz) {
        Object object = null;
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            object = unmarshaller.unmarshal(xmlSrc);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return (T) object;
    }
}
