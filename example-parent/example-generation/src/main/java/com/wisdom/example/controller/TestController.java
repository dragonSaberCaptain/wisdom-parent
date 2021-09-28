package com.wisdom.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisdom.example.service.TestService;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import com.wisdom.common.controller.BaseController;
import com.wisdom.example.entity.Test;
import lombok.extern.slf4j.Slf4j;
/**
 * Copyright ©2021 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 测试表 控制层
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2021-09-28 13:07:16 星期二
 */
@Slf4j
@Controller
@RequestMapping("/example/test")
@Api(value = "TestController API", tags = "test:测试表相关接口")
public class TestController extends BaseController<TestService, Test> {
    @Autowired
    private TestService testService;

}

