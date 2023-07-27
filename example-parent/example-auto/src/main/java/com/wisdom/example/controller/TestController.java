package com.wisdom.example.controller;

import com.wisdom.core.controller.BaseController;
import com.wisdom.example.entity.Test;
import com.wisdom.example.service.TestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试表 控制层
 *
 * @author captain
 * @version 1.0.0
 */
@RestController
@RequestMapping("/example/test")
public class TestController extends BaseController<TestService, Test> {

}

