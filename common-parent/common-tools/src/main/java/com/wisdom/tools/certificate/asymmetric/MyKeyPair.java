package com.wisdom.tools.certificate.asymmetric;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * 公私钥对 对象
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/24 10:50 星期五
 */
@Data
@Accessors(chain = true)
public class MyKeyPair {
    private String publicKey;
    private String privateKey;

    public MyKeyPair(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }
}
