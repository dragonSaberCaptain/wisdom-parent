package com.wisdom.openai.entity.completions;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * openai自定义模型请求对象
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/3/22 13:29 星期三
 */
@Slf4j
@Data
@Accessors(chain = true)
public class CompletionsReqEntity {
    /**
     * 可选参数,要使用的模型
     *
     * @mock text-davinci-003
     */
    private String model = "text-davinci-003";

    /**
     * 生成文本提示信息。
     *
     * @mock 你好!
     */
    @NotNull(message = "提示信息不能为空")
    private String prompt;

    /**
     * 可选参数,在生成文本完成后添加额外信息
     */
    private String suffix;

    /**
     * 可选参数,最大标记数。 范围：1 ~ 2048
     *
     * @mock 1024
     */
    private Integer max_tokens = 1024;

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

    /**
     * 可选参数,生成的答案数量。范围：1 ~ 2048
     *
     * @mock 1
     */
    private Integer n;

    /**
     * 可选参数,返回的方式,若设置为true,则返回结果会以流的形式逐步返回，而不是一次性返回所有结果。
     *
     * @mock false
     */
    private Boolean stream;

    /**
     * 可选参数，返回最有可能的令牌列表。最大为5
     *
     * @mock 5
     */
    private Integer logprobs;

    /**
     * 可选参数，回答时重复一遍提问.
     *
     * @mock false
     */
    private Boolean echo;

    /**
     * 可选参数，控制生成文本的终止条件。最多4个
     */
    private List<String> stop;

    /**
     * 可选参数，重复惩罚,范围:-2.0 ~ 2.0
     *
     * @mock 1.0
     */
    private Double presencePenalty;

    /**
     * 可选参数，频率惩罚
     *
     * @mock 1.0
     */
    private Double frequencyPenalty;

    /**
     * 可选参数，生成多个文本输出时，返回的最佳输出数量，并返回最佳结果
     */
    private Integer bestOf;

    /**
     * 可选参数，用于在生成文本时，对模型的输出进行微调以满足特定需求。
     */
    private Map<String, Integer> logitBias;

    /**
     * 可选参数，用于在请求中指定用户ID，以便API可以更好地跟踪和分析不同用户的使用情况。最大长度为64个字符
     */
    private String user;
}