package com.wisdom.openai.entity.model;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 * openai 模型详细信息响应对象
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/3/23 14:26 星期四
 */
@Slf4j
@Data
@Accessors(chain = true)
public class ModelRespEntity {
    /**
     * 此模型的标识符，用于在完成等操作时指定模型
     */
    private String id;
    /**
     * 返回的对象类型
     */
    private String object;
    /**
     * 创建时间（以epoch秒为单位）
     */
    private Long created;
    /**
     * 模型的所有者
     */
    private String ownedBy;
    /**
     * 此模型的权限列表
     */
    private List<PermissionRespEntity> permission;
    /**
     * 此及其父级（如果适用）所基于的根模型
     */
    private String root;
    /**
     * 此操作所基于的父模型
     */
    private Object parent;
}
