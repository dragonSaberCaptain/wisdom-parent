package com.wisdom.tools.algorithm.symmetric;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyPair;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * 非对称加密算法模板类用于:RSA、DSA、ECDSA
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/16 13:45 星期四
 */
@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SymmetricModel {
    /**
     * 当前生效的算法
     * 支持算法：AES
     */
    private String currentAlgorithm = "AES";

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

    SymmetricModel(String currentAlgorithm) {
        this.currentAlgorithm = currentAlgorithm;
    }

    public byte[] getDataSourceEncoded() {
        return dataSource.getBytes(StandardCharsets.UTF_8);
    }

    public byte[] getDataSourceEncryptEncoded() {
        return dataSourceEncrypt.getBytes(StandardCharsets.UTF_8);
    }
}