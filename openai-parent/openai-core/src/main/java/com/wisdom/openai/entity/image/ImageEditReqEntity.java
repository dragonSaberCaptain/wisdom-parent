package com.wisdom.openai.entity.image;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

/**
 * @author captain
 * @version 1.0
 * @description 图片模型编辑请求对象
 * @className ImageEditReqEntity
 * @projectName wisdom-parent
 * @packageName com.wisdom.openai.entity.image
 * @datetime 2023/4/11 13:06 星期二
 */
@Slf4j
@Data
@Accessors(chain = true)
public class ImageEditReqEntity {
    /**
     * 要编辑的图像。必须是有效的PNG文件，小于4MB，并且为正方形。若不提供遮罩，则图像必须具有透明度，透明度将用作遮罩。
     */
    @NotNull(message = "要编辑的图像不能为空")
    private String image;
    /**
     * 额外图像，指示图像应该在哪里编辑。必须是有效的PNG文件，小于4MB，并且具有与图像相同的尺寸
     */
    private String mask;
    /**
     * 提示信息
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
     * @mock 1024x1024
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
