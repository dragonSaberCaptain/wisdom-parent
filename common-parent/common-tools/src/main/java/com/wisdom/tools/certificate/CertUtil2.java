package com.wisdom.tools.certificate;

import com.alibaba.fastjson.JSONObject;
import com.wisdom.tools.string.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * 证书工具类
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/17 11:12 星期五
 */
@Slf4j
public class CertUtil2 {
    //----通用
    private static boolean IS_ASYMMETRIC_ALGORITHM = true; //是否是 非对称算法 默认:是非对称算法
    private static String CURRENT_ALGORITHM = ""; //当前算法
    private static String CURRENT_PWD = ""; //当前密码

    //----非对称算法相关
    private static final List<String> asymmetricAlgorithms = Arrays.asList("RSA", "DSA", "EC");

    /**
     * 对称算法相关 (算法/模式/填充模式)
     * 安全性: DES < DESede(3DES) < AES (推荐)
     * 模式: 5种
     * ----ECB模式(电子密码本模式):ECB模式是将明文消息分成固定大小的分组，当最后一个分组的内容小于分组长度时，需要用特定的数据进行填充以至于长度等于分组长度，每个分组的加密和解密都是独立的，可以进行并行操作，但是安全性较低
     * ----CBC模式(密码分组链接模式):CBC模式中的第一个分组需要用初始化向量IV（一个随机的且长度为一个分组长度的比特序列）进行异或操作再进行加密，而后面的每一个分组都要先和前一个分组加密后的密文分组进行异或操作，然后再加密。加密是连续的，不能进行并行操作。
     * ----CFB模式(密文反馈模式):CFB模式是将前一个分组的密文加密后和当前分组的明文进行异或操作生成当前分组的密文，第一个明文分组通过初始化向量lV进行加密再与之进行异或操作得到第一个密文分组。
     * ----OFB模式(输出反馈模式):OFB模式是通过将明文分组和密码算法的输出进行异或操作来产生密文分组的，也需要使用初始化向量（IV）
     * ----CTR模式(计数器模式):在CTR模式中，每次加密时都会生成一个不同的值来作为计数器的初始值，每个分组对应一个逐次累加的计数器，通过对计数器进行加密来生成密钥流，再将密钥流与明文分组进行异或操作得到密文分组
     * 填充模式:
     * ----NOPADDING(SunJCE支持):不填充，在此填充下原始数据必须是分组大小的整数倍，非整数倍时无法使用该模式
     * ----PKCS5PADDING(SunJCE支持)、PKCS7PADDING(BouncyCastle支持,简称BC):SunJCE 的 Provider 实现中 PKCS5PADDING 也按 PKCS7PADDING 来进行处理了。填充至符合块大小的整数倍，填充值为填充数量数。
     */
    private static final List<String> symmetricAlgorithms = Arrays.asList(
            "AES", "AES/CBC/NOPADDING", "AES/CBC/PKCS7PADDING", "AES/ECB/NOPADDING", "AES/ECB/PKCS7PADDING",
            "DES", "DES/CBC/NOPADDING", "DES/CBC/PKCS7PADDING", "DES/ECB/NOPADDING", "DES/ECB/PKCS7PADDING",
            "DESEDE", "DESEDE/CBC/NOPADDING", "DESEDE/CBC/PKCS7PADDING", "DESEDE/ECB/NOPADDING", "DESEDE/ECB/PKCS7PADDING");
    private static SecretKeySpec SECRET_KEY_SPEC = null;

    /**
     * 初始化
     *
     * @param pwd       密码
     * @param algorithm 算法
     * @author captain
     * @datetime 2022-03-07 16:20:52
     */
    public static void initCertUtil(String pwd, String algorithm) {
        addProvider();
        CURRENT_ALGORITHM = algorithm.toUpperCase(); //算法名称 转大写
        CURRENT_PWD = pwd.toUpperCase(); //算法密码 转大写
        if (symmetricAlgorithms.contains(CURRENT_ALGORITHM)) { //对称加密
            initSymmetricAlgorithm(pwd, CURRENT_ALGORITHM);
            IS_ASYMMETRIC_ALGORITHM = false;
            log.info("初始化成功,当前使用的算法:" + CURRENT_ALGORITHM);
        } else if (asymmetricAlgorithms.contains(CURRENT_ALGORITHM)) { //非对称加密
            log.info("初始化成功,当前使用的算法:" + CURRENT_ALGORITHM);
        } else {
            log.info("初始化失败,不支持该算法:" + CURRENT_ALGORITHM);
        }
    }

    /**
     * 生成对称加密秘钥
     */
    private static SecretKeySpec initSymmetricAlgorithm(String pwd, String algorithm) {
        String pwdMd5 = DigestUtils.md5Hex(pwd);
        if (null == SECRET_KEY_SPEC) {
            SECRET_KEY_SPEC = new SecretKeySpec(pwdMd5.getBytes(), algorithm);
        }
        return SECRET_KEY_SPEC;
    }


    /**
     * 对称加密算法
     *
     * @author captain
     * @datetime 2022-03-07 15:51:41
     */
    public static String encryptOrDecodeToBase64(String dataSource, boolean isEncrypt) {
        String resultStr = null;
        if (IS_ASYMMETRIC_ALGORITHM) {
            log.info("仅支持对称算法");
            return resultStr;
        }
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(CURRENT_ALGORITHM);
            if (isEncrypt) { // 加密(encrypt)
                if (CURRENT_ALGORITHM.contains("CBC") || CURRENT_ALGORITHM.contains("CFB") || CURRENT_ALGORITHM.contains("OFB")) {
                    IvParameterSpec iv = new IvParameterSpec(CURRENT_PWD.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
                    cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY_SPEC, iv);
                } else {
                    cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY_SPEC);
                }
                byte[] cipherBytes = cipher.doFinal(dataSource.getBytes(StandardCharsets.UTF_8));

                resultStr = Base64.encodeBase64String(cipherBytes);
            } else {  // 解密(decode)
                if (CURRENT_ALGORITHM.contains("CBC") || CURRENT_ALGORITHM.contains("CFB") || CURRENT_ALGORITHM.contains("OFB")) {
                    IvParameterSpec iv = new IvParameterSpec(StringUtil.addRightZero(CURRENT_PWD, 16).substring(0, 16).getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
                    cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY_SPEC, iv);
                } else {
                    cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY_SPEC);
                }
                byte[] dataDecodeBase64 = Base64.decodeBase64(dataSource.getBytes(StandardCharsets.UTF_8));
                resultStr = new String(cipher.doFinal(dataDecodeBase64));
            }
        } catch (Exception e) {
            log.error("CertUtil2--encryptToBase64", e);
        }
        return resultStr;
    }

    /**
     * 添加签名，验签，加密算法提供者
     */
    public static void addProvider() {
//        Security.removeProvider("BC");
//        Security.addProvider(new BouncyCastleProvider());
        printSysInfo();
    }

    /**
     * 打印系统环境信息
     */
    private static void printSysInfo() {
        Map<String, Object> systemInfoMap = new HashMap<>();
        systemInfoMap.put("os_name", System.getProperty("os.name"));
        systemInfoMap.put("os_arch", System.getProperty("os.arch"));
        systemInfoMap.put("os_version", System.getProperty("os.version"));
        systemInfoMap.put("java_vm_specification_version", System.getProperty("java.vm.specification.version"));
        systemInfoMap.put("java_vm_specification_vendor", System.getProperty("java.vm.specification.vendor"));
        systemInfoMap.put("java_vm_specification_name", System.getProperty("java.vm.specification.name"));
        systemInfoMap.put("java_vm_version", System.getProperty("java.vm.version"));
        systemInfoMap.put("java_vm_name", System.getProperty("java.vm.name"));
        systemInfoMap.put("java_version", System.getProperty("java.version"));
        systemInfoMap.put("java.vm.vendor", System.getProperty("java.vm.vendor"));
        systemInfoMap.put("providers", printProviders());
        log.info("系统信息(system info):" + JSONObject.toJSONString(systemInfoMap));
    }

    /**
     * 打印jre中算法提供者列表
     */
    private static String printProviders() {
        Provider[] providers = Security.getProviders();
        List<String> poviderName = Arrays.stream(providers).map(Provider::getName).collect(Collectors.toList());
        return StringUtil.join(poviderName, ",");
    }

    public static void main(String[] args) throws Exception {
        //----对称加密算法测试
        initCertUtil("dragonSaberCaptain", "AES/CBC/PKCS7PADDING");
        String encryptData = encryptOrDecodeToBase64("加密数据：挖槽", true);
        System.out.println("加密后的数据:" + encryptData);
        String decodeData = encryptOrDecodeToBase64(encryptData, false);
        System.out.println("解密后的数据:" + decodeData);
    }
}