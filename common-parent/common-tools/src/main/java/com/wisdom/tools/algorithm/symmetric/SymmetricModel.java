package com.wisdom.tools.algorithm.symmetric;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * 对称加密算法模板类用于:AES(默认)
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/16 13:45 星期四
 */
@Slf4j
@Data
public class SymmetricModel {
    public final String ALGORITHM_AES = "AES";

    /**
     * 当前生效的算法
     * 支持算法：AES
     */
    private String currentAlgorithm;

    /**
     * 加解密特有的算法:
     * AES：AES/ECB/PKCS7Padding
     */
    private String eadAlgorithm;

    /**
     * 加解密密码
     */
    private String key;

    /**
     * 密钥长度
     * AES:128、192、256
     */
    private int algorithmSize;

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

    SymmetricModel() {
        this.currentAlgorithm = ALGORITHM_AES;
        this.algorithmSize = 64 * 3; //192
        this.eadAlgorithm = "AES/ECB/PKCS7Padding";
        this.key = "dragonSaberCaptain";
    }

    public SymmetricModel initDefault(String currentAlgorithm) {
        SymmetricModel symmetricModel = new SymmetricModel();
        symmetricModel.setCurrentAlgorithm(currentAlgorithm);

        if (ALGORITHM_AES.equalsIgnoreCase(symmetricModel.getCurrentAlgorithm())) {
            this.algorithmSize = 64 * 3; //192
            this.eadAlgorithm = "AES/ECB/PKCS7Padding";
            return symmetricModel;
        }
        return null;
    }

    SymmetricModel(String currentAlgorithm, int algorithmSize, String eadAlgorithm) {
        this.currentAlgorithm = currentAlgorithm;
        this.algorithmSize = algorithmSize;
        this.eadAlgorithm = eadAlgorithm;
    }

    public byte[] getDataSourceEncoded() {
        return dataSource.getBytes(StandardCharsets.UTF_8);
    }

    public byte[] getDataSourceEncryptEncoded() {
        return dataSourceEncrypt.getBytes(StandardCharsets.UTF_8);
    }
}