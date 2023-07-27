package com.wisdom.openai.entity.embedding;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 输入文本的嵌入向量 请求对象
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/3/23 14:19 星期四
 */
@Slf4j
@Data
@Accessors(chain = true)
public class EmbeddingReqEntity {
    /**
     * 要使用的模型不能为空
     */
    @NotNull(message = "要使用的模型不能为空")
    private String model = "gpt-3.5-turbo";

    /**
     * 嵌入的文本
     *
     * @mock
     */
    @NotNull(message = "嵌入的文本不能为空")
    private List<String> input;

    /**
     * 可选参数，用于在请求中指定用户ID，以便API可以更好地跟踪和分析不同用户的使用情况。最大长度为64个字符
     */
    private String user;
}
