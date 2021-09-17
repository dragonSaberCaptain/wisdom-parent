package com.wisdom.tools.algorithm.symmetric;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * 支持的对称加密算法:AES
 * AES:密钥建立时间短、灵敏性好、内存需求低
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/16 13:45 星期四
 */
@Slf4j
public class SymmetricUtil {
    private static final String ALGORITHM_AES = "AES";


    public static void main(String[] args) throws Exception {
        SymmetricModel algorithmModel = new SymmetricModel(ALGORITHM_AES);
        algorithmModel.setKey("dragonSaberCaptain");

        algorithmModel.setDataSource("加密数据：挖槽");
        encryptData(algorithmModel);
        System.out.println("加密后的数据:" + algorithmModel.getDataSourceEncrypt());
//        algorithmModel.setKey("captain");
        decodeData(algorithmModel);
        System.out.println("解密后的数据:" + algorithmModel.getDataSourceDecrypt());

    }

    /**
     * 生成加密秘钥
     */

    private static SecretKeySpec initKey(SymmetricModel symmetricModel) {
        if (ALGORITHM_AES.equals(symmetricModel.getCurrentAlgorithm())) {
            if (symmetricModel.getAlgorithmSize() == 0) {
                symmetricModel.setAlgorithmSize(64 * 3);
            } else if (symmetricModel.getAlgorithmSize() > 256) { //秘钥最大长度
                symmetricModel.setAlgorithmSize(64 * 4);
            } else if (symmetricModel.getAlgorithmSize() < 128) { //秘钥最小长度
                symmetricModel.setAlgorithmSize(64 * 2);
            }
            if (StringUtils.isBlank(symmetricModel.getEadAlgorithm())) { //默认加解密算法
                symmetricModel.setEadAlgorithm("AES/ECB/PKCS7Padding");
            }
        }
        String keyMD5 = DigestUtils.md5Hex(symmetricModel.getKey());
        return new SecretKeySpec(keyMD5.getBytes(), symmetricModel.getCurrentAlgorithm());
    }

    public static SymmetricModel encryptData(SymmetricModel symmetricModel) throws Exception {

        SecretKeySpec secretKeySpec = initKey(symmetricModel);

        Security.addProvider(new BouncyCastleProvider());
        // 创建密码器
        Cipher cipher = Cipher.getInstance(symmetricModel.getEadAlgorithm());
        // 初始化为加密模式的密码
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] cipherBytes = cipher.doFinal(symmetricModel.getDataSourceEncoded());
        // 加密
        String encryptData = Base64.encodeBase64String(cipherBytes);
        symmetricModel.setDataSourceEncrypt(encryptData);
        return symmetricModel;
    }

    public static SymmetricModel decodeData(SymmetricModel symmetricModel) throws Exception {
        SecretKeySpec secretKeySpec = initKey(symmetricModel);

        Security.addProvider(new BouncyCastleProvider());
        // 创建密码器
        Cipher cipher = Cipher.getInstance(symmetricModel.getEadAlgorithm());
        // 使用密钥初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        // 解密
        byte[] dataDecodeBase64 = Base64.decodeBase64(symmetricModel.getDataSourceEncryptEncoded());
        String decryptData = new String(cipher.doFinal(dataDecodeBase64));
        symmetricModel.setDataSourceDecrypt(decryptData);
        return symmetricModel;
    }
}