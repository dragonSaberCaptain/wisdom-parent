package com.wisdom.example.controller;

import com.wisdom.common.controller.BaseController;
import com.wisdom.config.dto.ResultDto;
import com.wisdom.config.enums.HttpEnum;
import com.wisdom.example.entity.Test;
import com.wisdom.example.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * TODO (用一句话描述该类的作用)
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/10/27 9:52 星期三
 */
@Slf4j
@RestController
@RequestMapping("/example/test")
@Api(value = "TestController API ext", tags = "test:测试表相关接口扩展")
public class TestControllerExt {
    @Autowired
    private TestService testService;

    @GetMapping("/flow")
    @ApiOperation(value = "测试限流")
    public ResultDto test() {
        return new ResultDto<>(HttpEnum.OK);
    }
}
