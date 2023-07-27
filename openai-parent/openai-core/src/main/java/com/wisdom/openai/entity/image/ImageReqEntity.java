package com.wisdom.openai.entity.image;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

/**
 * @author captain
 * @version 1.0
 * @description 图片模型请求对象
 * @className ImageReqEntity
 * @projectName wisdom-parent
 * @packageName com.wisdom.openai.entity.image
 * @datetime 2023/4/11 11:02 星期二
 */
@Slf4j
@Data
@Accessors(chain = true)
public class ImageReqEntity {
    /**
     * 所需图像的文本描述
     *
     * @mock 画一只猫
     */
    @NotNull(message = "提示信息不能为空")
    private String prompt;

    /**
     * 要生成的图像数
     *
     * @mock 1
     */
    private Integer n;

    /**
     * 生成的图片大小,仅支持:256x256、512x512、1024x1024"
     *
     * @mock 256x256
     */
    private String size;

    /**
     * 生成的图片返回格式,仅支持:url、b64_json
     *
     * @mock url
     */
    private String response_format;

    /**
     * 可选参数，用于在请求中指定用户ID，以便API可以更好地跟踪和分析不同用户的使用情况。最大长度为64个字符
     */
    private String user;
}
