package com.wisdom.tools.cert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * TODO (用一句话描述该类的作用)
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/17 11:12 星期五
 */
@Slf4j
public class CertUtil {

    private static KeyStore getKeyStoreInfo(String pfxKeyFile, String keyPwd, String type) {
        log.info("开始加载签名证书==>" + pfxKeyFile);
        FileInputStream fis = null;
        try {
            KeyStore ks = KeyStore.getInstance(type);
            log.info("加载CertPath=[" + pfxKeyFile + "],Pwd=[" + keyPwd + "],type=[" + type + "]");
            fis = new FileInputStream(pfxKeyFile);
            char[] nPassword = null;
            if (StringUtils.isNotBlank(keyPwd.trim())) {
                nPassword = keyPwd.toCharArray();
            }
            ks.load(fis, nPassword);
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
        return null;
    }

}
