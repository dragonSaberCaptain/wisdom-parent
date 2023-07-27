package com.wisdom.openai.entity.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * @author captain
 * @version 1.0
 * @description 一个包含URL或64进制编码图像的对象
 * @className ImageEntity
 * @projectName wisdom-parent
 * @packageName com.wisdom.openai.entity.image
 * @datetime 2023/4/11 11:12 星期二
 */
@Slf4j
@Data
@Accessors(chain = true)
public class ImageEntity {
    /**
     * 图片地址
     *
     * @mock www.baidu.com
     */
    String url;

    /**
     * Base64 格式的图片字符串
     */
    @JsonProperty("b64_json")
    String b64Json;
}
