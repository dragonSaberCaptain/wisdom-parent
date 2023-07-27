package com.wisdom.openai.entity.model;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 此模型的权限列表
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/3/23 14:26 星期四
 */
@Slf4j
@Data
@Accessors(chain = true)
public class PermissionRespEntity {
    /**
     * 此模型权限的标识符
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
     * allowCreateEngine
     */
    private Boolean allowCreateEngine;

    /**
     * allowSampling
     */
    private Boolean allowSampling;

    /**
     * allowLogprobs
     */
    private Boolean allowLogprobs;

    /**
     * allowSearchIndices
     */
    private Boolean allowSearchIndices;

    /**
     * allowView
     */
    private Boolean allowView;

    /**
     * allowFineTuning
     */
    private Boolean allowFineTuning;

    /**
     * organization
     */
    private String organization;

    /**
     * group
     */
    private Object group;

    /**
     * isBlocking
     */
    private Boolean isBlocking;
}
