package com.wisdom.example.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisdom.example.service.TestServiceExt;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
/**
 * 测试表 控制层
 *
 * @author captain
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/example/test")
@Api(value = "TestController API", tags = "test:测试表 相关接口")
public class TestControllerExt {
    @Autowired
    private TestServiceExt testServiceExt;

}

