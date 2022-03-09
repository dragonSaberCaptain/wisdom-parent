package com.wisdom.tools.certificate;

import com.alibaba.fastjson.JSONObject;
import com.wisdom.tools.string.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.*;
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
public class CertUtil {
    private static final String KEY_TYPE = "PKCS12";
    private static KeyStore keyStore = null;
    private static String password = null;
    private static String keyAlias = null;

    public static void initCertUtil(String pfxKeyFile, String password, String type) {
        addProvider();
        if (keyStore == null) {
            keyStore = getKeyStoreInfo(pfxKeyFile, password, type);
        }
    }

    public static void initCertUtil(String pfxKeyFile, String keyPwd) {
        initCertUtil(pfxKeyFile, keyPwd, KEY_TYPE);
    }

    public static void initCertUtil(String pfxKeyFile) {
        initCertUtil(pfxKeyFile, null, KEY_TYPE);
    }

    /**
     * 添加签名，验签，加密算法提供者
     */
    public static void addProvider() {
        Security.removeProvider("BC");
        log.info("add BC provider");
        Security.addProvider(new BouncyCastleProvider());
        printSysInfo();
    }

    /**
     * 获得KeyStore
     *
     * @param pfxKeyFile 证书路径
     * @param pwd        证书密码
     * @param type       证书类型
     * @author captain
     * @datetime 2022-03-07 10:56:04
     */
    public static KeyStore getKeyStoreInfo(String pfxKeyFile, String pwd, String type) {
        FileInputStream fis = null;
        KeyStore ks = null;
        try {
            fis = new FileInputStream(pfxKeyFile);
            ks = KeyStore.getInstance(type);
            log.info("加载签名证书CertPath=[" + pfxKeyFile + "],pwd=[" + pwd + "],type=[" + type + "]");
            if (StringUtil.isNotBlank(pwd)) {
                password = pwd;
                ks.load(fis, password.toCharArray());
            } else {
                ks.load(fis, null);
            }
            return ks;
        } catch (Exception e) {
            log.error("getKeyStoreInfo Error", e);
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ks;
    }

    /**
     * 通过KeyStore,获取alias
     *
     * @return java.lang.String
     * @author captain
     * @datetime 2022-03-07 11:04:03
     */
    public static String getAlias() {
        try {
            if (keyAlias == null) {
                Enumeration<String> aliaseNum = keyStore.aliases();
                if (aliaseNum.hasMoreElements()) {
                    keyAlias = aliaseNum.nextElement();
                }
                log.info("CertUtil--getAlias:" + keyAlias);
            }
        } catch (KeyStoreException e) {
            log.error("CertUtil--getAlias", e);
        }
        return keyAlias;
    }

    /**
     * 通过KeyStore,获取证书公钥
     *
     * @return java.security.PublicKey
     * @author captain
     * @datetime 2022-03-07 10:59:51
     */
    public static PublicKey getPublicKey() {
        PublicKey publicKey = null;
        try {
            publicKey = keyStore.getCertificate(getAlias()).getPublicKey();
        } catch (KeyStoreException e) {
            log.error("CertUtil--getPublicKey", e);
        }
        return publicKey;
    }

    /**
     * 通过KeyStore,获取证书公钥转base64编码
     *
     * @return java.lang.String
     * @author captain
     * @datetime 2022-03-07 11:11:31
     */
    public static String getPublicKeyToBase64() {
        return Base64.encodeBase64String(getPublicKey().getEncoded());
    }

    /**
     * 通过KeyStore,获取证书私钥
     *
     * @return java.security.PrivateKey
     * @author captain
     * @datetime 2022-03-07 11:02:52
     */
    public static PrivateKey getPrivateKey() {
        PrivateKey privateKey = null;
        try {
            privateKey = (PrivateKey) keyStore.getKey(getAlias(), password.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            log.error("CertUtil--getPrivateKey", e);
        }
        return privateKey;
    }

    /**
     * 通过KeyStore,获取证书私钥转base64编码
     *
     * @return java.lang.String
     * @author captain
     * @datetime 2022-03-07 11:13:38
     */
    public static String getPrivateKeyToBase64() {
        return Base64.encodeBase64String(getPrivateKey().getEncoded());
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

    /**
     * 通过keystore获取私钥证书的certId值
     *
     * @return java.lang.String
     * @author captain
     * @datetime 2022-03-07 14:14:29
     */
    private static String getCertIdIdByStore() {
        try {
            X509Certificate cert = (X509Certificate) keyStore.getCertificate(getAlias());
            return String.valueOf(cert.getSerialNumber());
        } catch (KeyStoreException e) {
            log.error("getCertIdIdByStore Error", e);
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        String pfxKeyFile = "C:/app/hrbapp/app/datafile/unionpay/acp_test_sign_inst.pfx";
        String keyPwd = "000000";
        CertUtil.initCertUtil(pfxKeyFile, keyPwd);

        System.out.println("公钥信息：" + CertUtil.getPublicKeyToBase64());

        System.out.println("私钥信息：" + CertUtil.getPrivateKeyToBase64());

        System.out.println("certId值：" + CertUtil.getCertIdIdByStore());
    }
}