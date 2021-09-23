package com.wisdom.example.controller;

import com.wisdom.common.controller.BaseController;
import com.wisdom.example.entity.Test;
import com.wisdom.example.service.TestService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Copyright ©2021 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 测试表 控制层
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2021-09-23 09:43:03 星期四
 */
@Slf4j
@Controller
@RequestMapping("/example/test")
@Api(value = "TestController API", tags = "test:测试表相关接口")
public class TestController extends BaseController<TestService, Test> {
    @Resource
    private TestService testService;

}

