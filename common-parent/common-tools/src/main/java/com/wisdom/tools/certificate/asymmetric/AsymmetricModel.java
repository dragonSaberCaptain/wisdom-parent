package com.wisdom.tools.certificate.asymmetric;

import lombok.Data;
import lombok.experimental.Accessors;

import java.nio.charset.StandardCharsets;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * 非对称加密算法模板类用于:RSA、DSA、EC(即ECDSA,默认)
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/16 13:45 星期四
 */
@Data
@Accessors(chain = true)
public class AsymmetricModel {
    public final String ALGORITHM_EC = "EC";
    public final String ALGORITHM_RSA = "RSA";
    public final String ALGORITHM_DSA = "DSA";

    /**
     * 当前生效的算法
     * 支持算法：RSA、DSA、EC(即ECDSA,默认)
     */
    private String currentAlgorithm;

    /**
     * 支持的签名算法:
     * RSA有：SHA1WithRSA、SHA256withRSA、SHA512withRSA、MD5withRSA
     * DSA有：SHA1WithDSA、SHA256withDSA、SHA512withDSA、MD5withDSA
     * ECDSA有：SHA1WithECDSA、SHA256withECDSA(默认)、SHA512withECDSA、MD5withECDSA
     */
    private String signAlgorithm;

    /**
     * 加解密特有的算法:ECIES(EC即ECDSA)
     */
    private String eadAlgorithm;

    /**
     * 密钥长度 默认:64*32=2048
     * RSA:必须是64的倍数，在512到65536位之间
     * DSA:1024(max)
     * ECDSA:571(max)
     */
    private int algorithmSize;

    /**
     * 机数生成器(RNG)算法名称
     * SHA1PRNG
     */
    private String rngAlgorithm;
    /**
     * 默认的种子
     */
    private String defaultSeed;

    /**
     * 是否使用默认种子 默认使用:true
     */
    private boolean isUseSeed;

    /**
     * 密钥对
     */
    private MyKeyPair myKeyPair;

    /**
     * 数据源
     */
    private String dataSource;

    /**
     * 加密数据源
     */
    private String dataSourceEncrypt;

    /**
     * 解密数据源
     */
    private String dataSourceDecrypt;

    /**
     * 签名
     */
    private String sign;

    /**
     * 验签结果
     */
    private boolean signVerifyResult;

    /**
     * 是否支持加解密
     */
    private boolean openEad;

    public AsymmetricModel() {
        this.currentAlgorithm = ALGORITHM_EC;
        this.rngAlgorithm = "SHA1PRNG";
        this.defaultSeed = "dragonSaberCaptain";
        this.openEad = true;
        this.algorithmSize = 256;
        this.signAlgorithm = "SHA256withECDSA";
        this.eadAlgorithm = "ECIES";
    }

    public AsymmetricModel initDefault(String currentAlgorithm) {
        AsymmetricModel asymmetricModel = new AsymmetricModel();
        asymmetricModel.setCurrentAlgorithm(currentAlgorithm);
        if (ALGORITHM_EC.equalsIgnoreCase(asymmetricModel.getCurrentAlgorithm())) {
            return asymmetricModel; //EC算法直接返回
        }
        if (ALGORITHM_DSA.equalsIgnoreCase(asymmetricModel.getCurrentAlgorithm())) {
            asymmetricModel.setAlgorithmSize(64 * 12); //768
            asymmetricModel.setSignAlgorithm("SHA256withDSA");
            asymmetricModel.setOpenEad(false);
            return asymmetricModel;
        }
        if (ALGORITHM_RSA.equalsIgnoreCase(asymmetricModel.getCurrentAlgorithm())) {
            asymmetricModel.setAlgorithmSize(64 * 16); //1024
            asymmetricModel.setSignAlgorithm("SHA256withRSA");
            return asymmetricModel;
        }
        return null;
    }

    public byte[] getDataSourceEncoded() {
        return dataSource.getBytes(StandardCharsets.UTF_8);
    }
}