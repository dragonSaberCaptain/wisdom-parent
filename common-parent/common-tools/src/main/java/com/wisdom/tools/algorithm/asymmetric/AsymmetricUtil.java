package com.wisdom.tools.algorithm.asymmetric;

import com.wisdom.config.enums.HttpEnum;
import com.wisdom.config.exception.ResultException;
import com.wisdom.tools.datetime.DateUtilByZoned;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jcajce.provider.util.BadBlockException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
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

    private static final String ALGORITHM_EC = "EC";
    private static final String ALGORITHM_RSA = "RSA";
    private static final String ALGORITHM_DSA = "DSA";

    public static void main(String[] args) throws Exception {
        AsymmetricModel asymmetricModel = new AsymmetricModel(ALGORITHM_EC);
        //初始化密钥对
        AsymmetricUtil.initKeyPair(asymmetricModel);
        //公钥
        PublicKey publicKey = asymmetricModel.getKeyPair().getPublic();
        String publicKeyStr = getPublicKey(publicKey);
        //私钥
        PrivateKey privateKey = asymmetricModel.getKeyPair().getPrivate();
        String privateKeyStr = getPrivateKey(privateKey);

        System.out.println("公钥：/n" + publicKeyStr);
        System.out.println("私钥：/n" + privateKeyStr);

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

    public static AsymmetricModel init(AsymmetricModel asymmetricModel) throws Exception {
        if (ALGORITHM_RSA.equals(asymmetricModel.getCurrentAlgorithm())) {
            if (asymmetricModel.getAlgorithmSize() == 0) {
                asymmetricModel.setAlgorithmSize(64 * 16);
            } else if (asymmetricModel.getAlgorithmSize() > 65536) { //秘钥最大长度
                asymmetricModel.setAlgorithmSize(64 * 1024);
            } else if (asymmetricModel.getAlgorithmSize() < 512) { //秘钥最小长度
                asymmetricModel.setAlgorithmSize(64 * 8);
            }
            if (StringUtils.isBlank(asymmetricModel.getSignAlgorithm())) { //默认验签算法
                asymmetricModel.setSignAlgorithm("SHA256withRSA");
            }
        }

        if (ALGORITHM_DSA.equals(asymmetricModel.getCurrentAlgorithm())) {
            if (asymmetricModel.getAlgorithmSize() == 0) {
                asymmetricModel.setAlgorithmSize(64 * 16);
            } else if (asymmetricModel.getAlgorithmSize() > 1024) { //秘钥最大长度
                asymmetricModel.setAlgorithmSize(64 * 16);
            } else if (asymmetricModel.getAlgorithmSize() < 512) { //秘钥最小长度
                asymmetricModel.setAlgorithmSize(64 * 8);
            }
            if (StringUtils.isBlank(asymmetricModel.getSignAlgorithm())) { //默认验签算法
                asymmetricModel.setSignAlgorithm("SHA256withDSA");
            }
            //DSA算法不支持加解密功能
            asymmetricModel.setOpenEad(false);
        }

        if (ALGORITHM_EC.equals(asymmetricModel.getCurrentAlgorithm())) {
            if (asymmetricModel.getAlgorithmSize() == 0) {
                asymmetricModel.setAlgorithmSize(256); //默认秘钥长度
            } else if (asymmetricModel.getAlgorithmSize() > 571) { //秘钥最大长度
                asymmetricModel.setAlgorithmSize(571);
            }
            if (StringUtils.isBlank(asymmetricModel.getSignAlgorithm())) { //默认验签算法
                asymmetricModel.setSignAlgorithm("SHA256withECDSA");
            }

            if (StringUtils.isBlank(asymmetricModel.getEadAlgorithm())) { //默认加解密算法
                asymmetricModel.setEadAlgorithm("ECIES");
            }
        }
        return asymmetricModel;
    }

    /**
     * 初始化密钥对
     */
    public static AsymmetricModel initKeyPair(AsymmetricModel asymmetricModel) throws Exception {
        init(asymmetricModel);
        // 初始化随机产生器
        SecureRandom secureRandom = SecureRandom.getInstance(asymmetricModel.getRngAlgorithm());
        String seed = asymmetricModel.getDefaultSeed() + "@" + DateUtilByZoned.getNowDateUnMilli();
        secureRandom.setSeed(seed.getBytes());

        //实例化密钥生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(asymmetricModel.getCurrentAlgorithm());
        //初始化密钥生成器
        keyPairGenerator.initialize(asymmetricModel.getAlgorithmSize(), secureRandom);

        //生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        asymmetricModel.setKeyPair(keyPair);
        return asymmetricModel;
    }

    /**
     * 公钥加密
     *
     * @param asymmetricModel 参数模型
     * @author captain
     * @datetime 2021-09-16 17:57:13
     */
    public static AsymmetricModel publicKeyEncrypt(AsymmetricModel asymmetricModel) throws Exception {
        if (!asymmetricModel.isOpenEad()) {
            throw new ResultException(HttpEnum.METHOD_NOT_ALLOWED);
        }
        //取得转换公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(asymmetricModel.getPublicEncoded());
        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(asymmetricModel.getCurrentAlgorithm());
        //产生公钥
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);

        Cipher cipher;
        if (ALGORITHM_EC.equals(asymmetricModel.getCurrentAlgorithm())) {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            cipher = Cipher.getInstance(asymmetricModel.getEadAlgorithm());
        } else {
            cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        }
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        String encryptData = Base64.encodeBase64String(cipher.doFinal(asymmetricModel.getDataSourceEncoded()));
        asymmetricModel.setDataSourceEncrypt(encryptData);
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
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(asymmetricModel.getPrivateEncoded());
        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(asymmetricModel.getCurrentAlgorithm());
        //生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        Cipher cipher;
        if (ALGORITHM_EC.equals(asymmetricModel.getCurrentAlgorithm())) {
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
     * 取得私钥
     */
    public static String getPrivateKey(PrivateKey privateKey) {
        return Base64.encodeBase64String(privateKey.getEncoded());
    }

    /**
     * 取得公钥
     */
    public static String getPublicKey(PublicKey publicKey) {
        return Base64.encodeBase64String(publicKey.getEncoded());
    }

    /**
     * 用私钥进行数字签名
     */
    public static AsymmetricModel sign(AsymmetricModel asymmetricModel) throws Exception {
        // 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(asymmetricModel.getCurrentAlgorithm());

        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(asymmetricModel.getSignAlgorithm());

        // 取得转换私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(asymmetricModel.getPrivateEncoded());

        // 产生私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        signature.initSign(privateKey);

        signature.update(asymmetricModel.getDataSourceEncoded());
        String sign = Base64.encodeBase64String(signature.sign());
        asymmetricModel.setSign(sign);
        return asymmetricModel;
    }

    /**
     * 公钥验签
     */
    public static AsymmetricModel signVerify(AsymmetricModel asymmetricModel) throws Exception {
        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(asymmetricModel.getPublicEncoded());

        // 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(asymmetricModel.getCurrentAlgorithm());

        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance(asymmetricModel.getSignAlgorithm());
        signature.initVerify(pubKey);
        signature.update(asymmetricModel.getDataSourceEncoded());

        // 验证签名是否正常
        boolean verify = signature.verify(Base64.decodeBase64(asymmetricModel.getSign()));
        asymmetricModel.setSignVerifyResult(verify);
        return asymmetricModel;
    }
}