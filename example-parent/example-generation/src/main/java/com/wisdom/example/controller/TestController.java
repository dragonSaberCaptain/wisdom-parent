package com.wisdom.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisdom.example.service.TestService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import com.wisdom.common.controller.BaseController;
import com.wisdom.example.entity.Test;
/**
 * 测试表 控制层
 *
 * @author captain
 * @version 1.0
 */
@RestController
@RequestMapping("/example/test")
@Api(value = "TestController API", tags = "test:测试表 相关接口")
public class TestController extends BaseController<TestService, Test> {

}

