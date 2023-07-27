package com.wisdom.openai.entity.file;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 上传到OpenAi的文件对象
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/3/23 14:26 星期四
 */
@Slf4j
@Data
@Accessors(chain = true)
public class FileEntity {
    /**
     * 文件唯一id
     */
    private String id;

    /**
     * 返回的对象类型
     */
    private String object;

    /**
     * 文件大小,即文件字节大小
     */
    private Long bytes;

    /**
     * 创建时间（以epoch秒为单位）
     */
    private Long createdAt;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 文件用途描述
     */
    private String purpose;
}
