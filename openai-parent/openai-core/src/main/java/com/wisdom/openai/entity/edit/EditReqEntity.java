package com.wisdom.openai.entity.edit;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

/**
 * 给定提示和指令，OpenAi将返回经过编辑的提示版本
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/3/22 13:29 星期三
 */
@Slf4j
@Data
@Accessors(chain = true)
public class EditReqEntity {
    /**
     * 要使用的模型
     */
    @NotNull(message = "要使用的模型不能为空")
    private String model;

    /**
     * 要用作编辑起点的输入文本
     */
    private String input;

    /**
     * 告诉模型如何编辑提示的指令。例如：修复拼写错误
     *
     * @mock 修复拼写错误
     */
    @NotNull(message = "编辑指令不能为空")
    private String instruction;

    /**
     * 可选参数,生成的答案数量。默认值:1。范围：1 ~ 2048
     *
     * @mock 1
     */
    private Integer n;

    /**
     * 可选参数,温度采样,与topP参数互斥,值越高越随机。范围: 0.0 ~ 2.0
     *
     * @mock 1.0
     */
    private Double temperature;

    /**
     * 可选参数,核采样,与temperature参数互斥,值越高越随机,范围: 0.0 ~ 1.0
     *
     * @mock 1.0
     */
    private Double topP;
}
