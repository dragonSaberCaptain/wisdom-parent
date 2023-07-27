package com.wisdom.tools.certificate.sm;

import cn.hutool.crypto.asymmetric.SM2;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * @author captain
 * @version 1.0
 * @description TODO (用一句话描述该类的作用)
 * @className TestUtil
 * @projectName wisdom-parent
 * @packageName com.wisdom.tools.certificate.sm
 * @datetime 2023/5/22 13:38 星期一
 */
@Slf4j
@Data
@Accessors(chain = true)
public class TestUtil {
    public static void main(String[] args) {
        String data = "dPhq2XdoMcgD5m7M0I51SX7MkzMerWMcPdBdv/tX8B5jOyM28n+CcXUn721/9N0ELVgC2P0eBRn4jD04rPScJd5izcC7+xXT5LUwbV2S6wc0g2RC8nkuZITc4rdrACPvNxd18b6y";
        String pub = "0417f347d7fa08ae6ad9bf8ef6ac6c313810e05044290f7c18dc9b913b252603505cf7cdbf7ac7d88de508e78bbc2d74cb28c0a90724ed4b751cc69bdfe55b68de";
        String pri = "73d76cf4f553535d6ec45478fb1581baa0c83e166b347af10ab129966d3f187f";
        String key = "0123456789abcdeffedcba9876543210";

        SM2 sm2 = new SM2(pri, pub);
        byte[] dec = sm2.decrypt(data.getBytes());
        String sm4Key = new String(dec);
        System.out.println(sm4Key);
    }
}
