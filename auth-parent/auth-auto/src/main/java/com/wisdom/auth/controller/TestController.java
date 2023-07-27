package com.wisdom.auth.controller;

import com.wisdom.auth.entity.Test;
import com.wisdom.auth.service.TestService;
import com.wisdom.core.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试表 控制层
 *
 * @author captain
 * @version 1.0.0
 */
@RestController
@RequestMapping("/auth/test")
public class TestController extends BaseController<TestService, Test> {

}

