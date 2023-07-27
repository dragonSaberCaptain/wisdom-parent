package com.wisdom.openai.entity.file;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author captain
 * @version 1.0
 * @description 文件删除返回对象
 * @className FileDeleteEntity
 * @projectName wisdom-parent
 * @packageName com.wisdom.openai.entity.file
 * @datetime 2023/4/7 15:53 星期五
 */
@NoArgsConstructor
@Data
public class FileDeleteRespEntity {
    /**
     * 返回的对象类型
     *
     * @mock file
     */
    private String object;
    /**
     * 删除的文件id
     *
     * @mock file-L21HnxsFHn6MSBWZwgSq6zgs
     */
    private String id;
    /**
     * 是否删除
     *
     * @mock true
     */
    private Boolean deleted;
}
