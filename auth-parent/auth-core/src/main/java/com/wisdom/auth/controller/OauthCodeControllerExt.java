package com.wisdom.auth.controller;

import com.wisdom.auth.service.OauthCodeServiceExt;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 控制层
 *
 * @author captain
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/auth/oauthCode")
@Api(value = "OauthCodeController API", tags = "oauth_code: 相关接口")
public class OauthCodeControllerExt {
    @Autowired
    private OauthCodeServiceExt oauthCodeServiceExt;

}

