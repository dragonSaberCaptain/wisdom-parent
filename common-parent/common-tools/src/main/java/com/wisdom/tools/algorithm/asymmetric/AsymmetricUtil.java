package com.wisdom.tools.algorithm.asymmetric;

import com.wisdom.config.enums.DateTimeEnum;
import com.wisdom.config.enums.HttpEnum;
import com.wisdom.config.exception.ResultException;
import com.wisdom.tools.datetime.DateUtilByZoned;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * 支持的非对称加密算法：RSA、DSA、EC(即ECDSA)
 * RSA:数字加解密和签名算法 主流可选秘钥长度为 1024位、2048位(推荐)、4096位等
 * DSA:数字签名算法 仅能用于数字签名,不能进行数据加密解密 DSA的一个重要特点是两个素数公开，这样，当使用别人的p和q时，即使不知道私钥，你也能确认它们是否是随机产生的，还是作了手脚。
 * ECDSA简称EC:椭圆曲线签名算法 更小的秘钥，更高的效率，提供更高的安全保障，据称256位的ECC秘钥的安全性等同于3072位的RSA秘钥
 * <p>
 * 公钥加密、私钥解密是密送，保证消息即使公开也只有私钥持有者能读懂。
 * 私钥加密、公钥解密是签名，保证消息来源是私钥持有者。
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/16 13:45 星期四
 */
@Slf4j
public class AsymmetricUtil {

    public static void main(String[] args) throws Exception {
        AsymmetricModel asymmetricModel = new AsymmetricModel().initDefault("EC");
        if (asymmetricModel == null) {
            System.out.println("暂不支持该算法");
            return;
        }
        //初始化密钥对
        AsymmetricUtil.initKeyPair(asymmetricModel);

        if (asymmetricModel.isOpenEad()) {
            System.out.println("================模拟服务器向用户发送加密数据==============");
            asymmetricModel.setDataSource("数据内容：只有指定用户可以看到，其他人不能看");
            System.out.println("数据原文:" + asymmetricModel.getDataSource());
            System.out.println("================服务器使用公钥对数据加密==============");
            publicKeyEncrypt(asymmetricModel);
            System.out.println("服务器公钥加密后的密文：" + asymmetricModel.getDataSourceEncrypt());

            System.out.println("================服务器数据在网络上开始传输==============");
//            System.out.println("================模拟篡改数据==============");
//            String str = "数据内容：怕不是有py交易哦，不让其他人看";
//            String pyTrade = Base64.encodeBase64String(str.getBytes());
//            asymmetricModel.setDataSourceEncrypt(pyTrade);

            System.out.println("================用户使用私钥解密数据==============");
            privateKeyDecrypt(asymmetricModel);
            System.out.println("解密后的数据：" + asymmetricModel.getDataSourceDecrypt());
        }

        System.out.println("================用户接受到数据处理后向服务器返回数据==============");
        asymmetricModel.setDataSource("数据内容：只许服务器可以看，其他人不能看");
        sign(asymmetricModel);
        System.out.println("用户私钥签名后的密文:" + asymmetricModel.getSign());

        System.out.println("================用户数据在网络上开始传输==============");

//        System.out.println("================模拟篡改数据==============");
//        asymmetricModel.setDataSource("数据内容：凭什么服务器可以看，其他人不能看");

        System.out.println("================服务器使用公钥验签==============");
        signVerify(asymmetricModel);
        System.out.println("服务器公钥验签结果:" + asymmetricModel.isSignVerifyResult());
    }

    /**
     * 初始化密钥对
     */
    public static AsymmetricModel initKeyPair(AsymmetricModel asymmetricModel) {
        try {
            // 初始化随机产生器
            SecureRandom secureRandom = null;

            secureRandom = SecureRandom.getInstance(asymmetricModel.getRngAlgorithm());

            String seed = asymmetricModel.getDefaultSeed() + "@" + DateUtilByZoned.getDateTime(DateTimeEnum.DATETIME_PATTERN_MILLI_UN);
            secureRandom.setSeed(seed.getBytes());

            //实例化密钥生成器
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(asymmetricModel.getCurrentAlgorithm());
            //初始化密钥生成器
            keyPairGenerator.initialize(asymmetricModel.getAlgorithmSize(), secureRandom);

            //生成密钥对
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            String publicKey = Base64.encodeBase64String(keyPair.getPublic().getEncoded());
            String privateKey = Base64.encodeBase64String(keyPair.getPrivate().getEncoded());
            asymmetricModel.setMyKeyPair(new MyKeyPair(publicKey, privateKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return asymmetricModel;
    }

    /**
     * 公钥加密
     *
     * @param asymmetricModel 参数模型
     * @author captain
     * @datetime 2021-09-16 17:57:13
     */
    public static AsymmetricModel publicKeyEncrypt(AsymmetricModel asymmetricModel) {
        try {
            if (!asymmetricModel.isOpenEad()) {
                throw new ResultException(HttpEnum.METHOD_NOT_ALLOWED);
            }
            //取得转换公钥
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(asymmetricModel.getMyKeyPair().getPublicKey()));
            //实例化密钥工厂
            KeyFactory keyFactory = null;

            keyFactory = KeyFactory.getInstance(asymmetricModel.getCurrentAlgorithm());

            //产生公钥
            PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);

            Cipher cipher;
            if (asymmetricModel.ALGORITHM_EC.equals(asymmetricModel.getCurrentAlgorithm())) {
                Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
                cipher = Cipher.getInstance(asymmetricModel.getEadAlgorithm());
            } else {
                cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            }
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            String encryptData = Base64.encodeBase64String(cipher.doFinal(asymmetricModel.getDataSourceEncoded()));
            asymmetricModel.setDataSourceEncrypt(encryptData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return asymmetricModel;
    }

    /**
     * 私钥解密
     *
     * @param asymmetricModel 参数模型
     * @author admin
     * @datetime 2021-09-17 10:23:56
     */
    public static AsymmetricModel privateKeyDecrypt(AsymmetricModel asymmetricModel) throws Exception {
        if (!asymmetricModel.isOpenEad()) {
            throw new ResultException(HttpEnum.METHOD_NOT_ALLOWED);
        }
        //取得转换私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(asymmetricModel.getMyKeyPair().getPrivateKey()));
        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(asymmetricModel.getCurrentAlgorithm());
        //生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        Cipher cipher;
        if (asymmetricModel.ALGORITHM_EC.equals(asymmetricModel.getCurrentAlgorithm())) {
            Security.addProvider(new BouncyCastleProvider());
            cipher = Cipher.getInstance(asymmetricModel.getEadAlgorithm());
        } else {
            cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        }
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] dataDecodeBase64 = Base64.decodeBase64(asymmetricModel.getDataSourceEncrypt());

        String decryptData = new String(cipher.doFinal(dataDecodeBase64));

        asymmetricModel.setDataSourceDecrypt(decryptData);
        return asymmetricModel;
    }

    /**
     * 用私钥进行数字签名
     */
    public static AsymmetricModel sign(AsymmetricModel asymmetricModel) {
        try {
            // 指定的加密算法
            KeyFactory keyFactory = null;

            keyFactory = KeyFactory.getInstance(asymmetricModel.getCurrentAlgorithm());

            // 用私钥对信息生成数字签名
            Signature signature = Signature.getInstance(asymmetricModel.getSignAlgorithm());

            // 取得转换私钥
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(asymmetricModel.getMyKeyPair().getPrivateKey()));

            // 产生私钥
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
            signature.initSign(privateKey);

            signature.update(asymmetricModel.getDataSourceEncoded());
            String sign = Base64.encodeBase64String(signature.sign());
            asymmetricModel.setSign(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return asymmetricModel;
    }

    /**
     * 公钥验签
     */
    public static AsymmetricModel signVerify(AsymmetricModel asymmetricModel) {
        try {
            // 构造X509EncodedKeySpec对象
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(asymmetricModel.getMyKeyPair().getPublicKey()));

            // 指定的加密算法
            KeyFactory keyFactory = null;

            keyFactory = KeyFactory.getInstance(asymmetricModel.getCurrentAlgorithm());

            // 取公钥匙对象
            PublicKey pubKey = keyFactory.generatePublic(keySpec);

            Signature signature = Signature.getInstance(asymmetricModel.getSignAlgorithm());
            signature.initVerify(pubKey);
            signature.update(asymmetricModel.getDataSourceEncoded());

            // 验证签名是否正常
            boolean verify = signature.verify(Base64.decodeBase64(asymmetricModel.getSign()));
            asymmetricModel.setSignVerifyResult(verify);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return asymmetricModel;
    }
}