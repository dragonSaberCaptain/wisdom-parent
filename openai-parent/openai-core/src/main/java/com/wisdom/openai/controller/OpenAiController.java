package com.wisdom.openai.controller;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.wisdom.base.dto.ResultDto;
import com.wisdom.base.enums.HttpEnum;
import com.wisdom.base.enums.ResultEnum;
import com.wisdom.base.exception.ResultException;
import com.wisdom.openai.config.NacosConfig;
import com.wisdom.openai.entity.OpenAiRespEntity;
import com.wisdom.openai.entity.OpenaiErrorRespEntity;
import com.wisdom.openai.entity.completions.CompletionsReqEntity;
import com.wisdom.openai.entity.completions.CompletionsRespEntity;
import com.wisdom.openai.entity.completions.chat.ChatCompletionReqEntity;
import com.wisdom.openai.entity.completions.chat.ChatCompletionsRespEntity;
import com.wisdom.openai.entity.edit.EditReqEntity;
import com.wisdom.openai.entity.edit.EditRespEntity;
import com.wisdom.openai.entity.embedding.EmbeddingReqEntity;
import com.wisdom.openai.entity.embedding.EmbeddingRespEntity;
import com.wisdom.openai.entity.file.FileDeleteRespEntity;
import com.wisdom.openai.entity.file.FileEntity;
import com.wisdom.openai.entity.image.ImageEditReqEntity;
import com.wisdom.openai.entity.image.ImageReqEntity;
import com.wisdom.openai.entity.image.ImageRespEntity;
import com.wisdom.openai.entity.model.ModelRespEntity;
import com.wisdom.openai.tools.OpenAiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * openai相关接口
 *
 * @author captain
 * @version 1.0
 * @description 代理服务:api.openai.com = captain-ai.club
 * @className OpenAiController
 * @projectName wisdom-parent
 * @packageName com.wisdom.openai.controller
 * @datetime 2023/3/10 15:44 星期五
 */
@Slf4j
@RestController
public class OpenAiController {
    @Autowired
    private NacosConfig nacosConfig;

    /**
     * 获取可用模型列表
     *
     * @author captain
     * @datetime 2023-04-06 13:42:40
     */
    @GetMapping(value = "/openai/listModels")
    public ResultDto<List<ModelRespEntity>> listModels() {
        String fullUrl = nacosConfig.getBaseUrl() + "/v1/models";
        String body = OpenAiUtil.sendGetData(fullUrl, nacosConfig.getToken());
        OpenAiRespEntity<ModelRespEntity> openAiRespEntity = JSONUtil.toBean(body, new TypeReference<OpenAiRespEntity<ModelRespEntity>>() {
        }, false);
        if (null == openAiRespEntity.getData()) {
            OpenaiErrorRespEntity errorData = JSONUtil.toBean(body, new TypeReference<OpenaiErrorRespEntity>() {
            }, false);
            throw new ResultException(ResultEnum.RESULT_ENUM_1002, errorData);
        }
        return new ResultDto<>(HttpEnum.OK, openAiRespEntity.getData());
    }

    /**
     * 获取可用模型
     *
     * @param modelId 模型id
     * @author captain
     * @datetime 2023-04-06 13:43:16
     */
    @GetMapping(value = "/openai/getModel")
    public ResultDto<ModelRespEntity> getModel(@RequestParam(value = "modelId") String modelId) {
        String fullUrl = nacosConfig.getBaseUrl() + "/v1/models/" + modelId;
        String body = OpenAiUtil.sendGetData(fullUrl, nacosConfig.getToken());
        ModelRespEntity resultData = JSONUtil.toBean(body, new TypeReference<ModelRespEntity>() {
        }, false);
        if (null == resultData.getId()) {
            OpenaiErrorRespEntity errorData = JSONUtil.toBean(body, new TypeReference<OpenaiErrorRespEntity>() {
            }, false);
            throw new ResultException(ResultEnum.RESULT_ENUM_1002, errorData);
        }
        return new ResultDto<>(HttpEnum.OK, resultData);
    }

    /**
     * 使用自定义模型建立连接
     *
     * @param request
     * @author captain
     * @datetime 2023-04-06 13:44:13
     */
    @PostMapping(value = "/openai/createCompletion")
    public ResultDto<CompletionsRespEntity> createCompletion(@RequestBody CompletionsReqEntity request) {
        String fullUrl = nacosConfig.getBaseUrl() + "/v1/completions";
        String body = OpenAiUtil.sendPostData(fullUrl, nacosConfig.getToken(), JSONUtil.toJsonStr(request));
        CompletionsRespEntity resultData = JSONUtil.toBean(body, new TypeReference<CompletionsRespEntity>() {
        }, false);
        if (null == resultData.getId()) {
            OpenaiErrorRespEntity errorData = JSONUtil.toBean(body, new TypeReference<OpenaiErrorRespEntity>() {
            }, false);
            throw new ResultException(ResultEnum.RESULT_ENUM_1002, errorData);
        }
        return new ResultDto<>(HttpEnum.OK, resultData);
    }

    /**
     * 使用支持聊天模型建立连接
     *
     * @param request
     * @author captain
     * @datetime 2023-04-06 13:44:13
     */
    @PostMapping(value = "/openai/createChatCompletion")
    public ResultDto<ChatCompletionsRespEntity> createChatCompletion(@RequestBody ChatCompletionReqEntity request) {
        String fullUrl = nacosConfig.getBaseUrl() + "/v1/chat/completions";
        String body = OpenAiUtil.sendPostData(fullUrl, nacosConfig.getToken(), JSONUtil.toJsonStr(request));
        ChatCompletionsRespEntity resultData = JSONUtil.toBean(body, new TypeReference<ChatCompletionsRespEntity>() {
        }, false);
        if (null == resultData.getId()) {
            OpenaiErrorRespEntity errorData = JSONUtil.toBean(body, new TypeReference<OpenaiErrorRespEntity>() {
            }, false);
            throw new ResultException(ResultEnum.RESULT_ENUM_1002, errorData);
        }
        return new ResultDto<>(HttpEnum.OK, resultData);
    }

    /**
     * 使用支持编辑模型建立连接
     *
     * @param request
     * @author captain
     * @datetime 2023-04-06 13:44:13
     */
    @PostMapping(value = "/openai/createEdit")
    public ResultDto<EditRespEntity> createEdit(@RequestBody EditReqEntity request) {
        String fullUrl = nacosConfig.getBaseUrl() + "/v1/edits";
        String body = OpenAiUtil.sendPostData(fullUrl, nacosConfig.getToken(), JSONUtil.toJsonStr(request));
        EditRespEntity resultData = JSONUtil.toBean(body, new TypeReference<EditRespEntity>() {
        }, false);
        if (null == resultData.getObject()) {
            OpenaiErrorRespEntity errorData = JSONUtil.toBean(body, new TypeReference<OpenaiErrorRespEntity>() {
            }, false);
            throw new ResultException(ResultEnum.RESULT_ENUM_1002, errorData);
        }
        return new ResultDto<>(HttpEnum.OK, resultData);
    }

    /**
     * 使用支持嵌入向量模型建立连接
     *
     * @param request
     * @author captain
     * @datetime 2023-04-06 13:47:29
     */
    @PostMapping(value = "/openai/createEmbeddings")
    public ResultDto<EmbeddingRespEntity> createEmbeddings(@RequestBody EmbeddingReqEntity request) {
        String fullUrl = nacosConfig.getBaseUrl() + "/v1/embeddings";
        String body = OpenAiUtil.sendPostData(fullUrl, nacosConfig.getToken(), JSONUtil.toJsonStr(request));
        EmbeddingRespEntity resultData = JSONUtil.toBean(body, new TypeReference<EmbeddingRespEntity>() {
        }, false);
        if (null == resultData.getObject()) {
            OpenaiErrorRespEntity errorData = JSONUtil.toBean(body, new TypeReference<OpenaiErrorRespEntity>() {
            }, false);
            throw new ResultException(ResultEnum.RESULT_ENUM_1002, errorData);
        }
        return new ResultDto<>(HttpEnum.OK, resultData);
    }

    /**
     * 获取上传的文件列表
     *
     * @author captain
     * @datetime 2023-04-06 13:47:29
     */
    @GetMapping(value = "/openai/listFiles")
    public ResultDto<List<FileEntity>> listFiles() {
        String fullUrl = nacosConfig.getBaseUrl() + "/v1/files";
        String body = OpenAiUtil.sendGetData(fullUrl, nacosConfig.getToken());
        OpenAiRespEntity<FileEntity> openAiRespEntity = JSONUtil.toBean(body, new TypeReference<OpenAiRespEntity<FileEntity>>() {
        }, false);
        if (null == openAiRespEntity.getData()) {
            OpenaiErrorRespEntity errorData = JSONUtil.toBean(body, new TypeReference<OpenaiErrorRespEntity>() {
            }, false);
            throw new ResultException(ResultEnum.RESULT_ENUM_1002, errorData);
        }
        return new ResultDto<>(HttpEnum.OK, openAiRespEntity.getData());
    }

    /**
     * 文件上传
     *
     * @param purpose       文件目的
     * @param multipartFile 文件上传对象流
     * @author captain
     * @datetime 2023-04-06 14:15:09
     */
    @PostMapping(value = "/openai/uploadFile")
    public ResultDto<FileEntity> uploadFile(@RequestParam(value = "purpose", defaultValue = "fine-tune") String purpose, @RequestParam("file") MultipartFile multipartFile) {
        String fullUrl = nacosConfig.getBaseUrl() + "/v1/files";
        File file = OpenAiUtil.dealMultipartFileToFile(multipartFile);
        String body = OpenAiUtil.sendPostFile(fullUrl, nacosConfig.getToken(), purpose, file);
        FileEntity resultData = JSONUtil.toBean(body, new TypeReference<FileEntity>() {
        }, false);
        if (null == resultData.getId()) {
            OpenaiErrorRespEntity errorData = JSONUtil.toBean(body, new TypeReference<OpenaiErrorRespEntity>() {
            }, false);
            throw new ResultException(ResultEnum.RESULT_ENUM_1002, errorData);
        }
        return new ResultDto<>(HttpEnum.OK, resultData);
    }

    /**
     * 删除上传文件
     *
     * @param fileId 文件唯一id
     * @return ResultDto<FileEntity>
     * @author captain
     * @datetime 2023-04-07 15:45:02
     */
    @DeleteMapping(value = "/openai/deleteFile")
    public ResultDto<FileDeleteRespEntity> deleteFile(@RequestParam(value = "fileId") String fileId) {
        String fullUrl = nacosConfig.getBaseUrl() + "/v1/files/" + fileId;
        String body = OpenAiUtil.sendDeleteData(fullUrl, nacosConfig.getToken());
        FileDeleteRespEntity resultData = JSONUtil.toBean(body, new TypeReference<FileDeleteRespEntity>() {
        }, false);
        if (null == resultData.getId()) {
            OpenaiErrorRespEntity errorData = JSONUtil.toBean(body, new TypeReference<OpenaiErrorRespEntity>() {
            }, false);
            throw new ResultException(ResultEnum.RESULT_ENUM_1002, errorData);
        }
        return new ResultDto<>(HttpEnum.OK, resultData);
    }

    /**
     * 检索上传的文件
     *
     * @param fileId 文件唯一id
     * @return ResultDto<FileDeleteRespEntity>
     * @author captain
     * @datetime 2023-04-07 15:58:19
     */
    @GetMapping(value = "/openai/retrieveFile")
    public ResultDto<FileEntity> retrieveFile(@RequestParam(value = "fileId") String fileId) {
        String fullUrl = nacosConfig.getBaseUrl() + "/v1/files/" + fileId;
        String body = OpenAiUtil.sendDeleteData(fullUrl, nacosConfig.getToken());
        FileEntity resultData = JSONUtil.toBean(body, new TypeReference<FileEntity>() {
        }, false);
        if (null == resultData.getId()) {
            OpenaiErrorRespEntity errorData = JSONUtil.toBean(body, new TypeReference<OpenaiErrorRespEntity>() {
            }, false);
            throw new ResultException(ResultEnum.RESULT_ENUM_1002, errorData);
        }
        return new ResultDto<>(HttpEnum.OK, resultData);
    }

    /**
     * 生成图片
     *
     * @param request
     * @return ResultDto<ImageRespEntity>
     * @author captain
     * @datetime 2023-04-11 11:15:28
     */
    @PostMapping(value = "/openai/createImage")
    public ResultDto<ImageRespEntity> createImage(@RequestBody ImageReqEntity request) {
        String fullUrl = nacosConfig.getBaseUrl() + "/v1/images/generations";
        String body = OpenAiUtil.sendPostData(fullUrl, nacosConfig.getToken(), JSONUtil.toJsonStr(request));
        ImageRespEntity resultData = JSONUtil.toBean(body, new TypeReference<ImageRespEntity>() {
        }, false);
        if (null == resultData.getData()) {
            OpenaiErrorRespEntity errorData = JSONUtil.toBean(body, new TypeReference<OpenaiErrorRespEntity>() {
            }, false);
            throw new ResultException(ResultEnum.RESULT_ENUM_1002, errorData);
        }
        return new ResultDto<>(HttpEnum.OK, resultData);
    }

    /**
     * 编辑图片
     *
     * @param request
     * @return ResultDto<ImageRespEntity>
     * @author captain
     * @datetime 2023-04-11 13:17:31
     */
    @PostMapping(value = "/openai/createImageEdit")
    public ResultDto<ImageRespEntity> createImageEdit(@RequestBody ImageEditReqEntity request) {
        String fullUrl = nacosConfig.getBaseUrl() + "/v1/images/edits";
        String body = OpenAiUtil.sendPostData(fullUrl, nacosConfig.getToken(), JSONUtil.toJsonStr(request));
        ImageRespEntity resultData = JSONUtil.toBean(body, new TypeReference<ImageRespEntity>() {
        }, false);
        if (null == resultData.getData()) {
            OpenaiErrorRespEntity errorData = JSONUtil.toBean(body, new TypeReference<OpenaiErrorRespEntity>() {
            }, false);
            throw new ResultException(ResultEnum.RESULT_ENUM_1002, errorData);
        }
        return new ResultDto<>(HttpEnum.OK, resultData);
    }

    /**
     * 变化图片
     *
     * @param request
     * @return ResultDto<ImageRespEntity>
     * @author captain
     * @datetime 2023-04-11 13:18:23
     */
    @PostMapping(value = "/openai/createImageVariation")
    public ResultDto<ImageRespEntity> createImageVariation(@RequestBody ImageEditReqEntity request) {
        String fullUrl = nacosConfig.getBaseUrl() + "/v1/images/variations";
        String body = OpenAiUtil.sendPostData(fullUrl, nacosConfig.getToken(), JSONUtil.toJsonStr(request));
        ImageRespEntity resultData = JSONUtil.toBean(body, new TypeReference<ImageRespEntity>() {
        }, false);
        if (null == resultData.getData()) {
            OpenaiErrorRespEntity errorData = JSONUtil.toBean(body, new TypeReference<OpenaiErrorRespEntity>() {
            }, false);
            throw new ResultException(ResultEnum.RESULT_ENUM_1002, errorData);
        }
        return new ResultDto<>(HttpEnum.OK, resultData);
    }
}
