package com.wisdom.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisdom.auth.service.OauthApprovalsServiceExt;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
/**
 *  控制层
 *
 * @author captain
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/auth/oauthApprovals")
@Api(value = "OauthApprovalsController API", tags = "oauth_approvals: 相关接口")
public class OauthApprovalsControllerExt {
    @Autowired
    private OauthApprovalsServiceExt oauthApprovalsServiceExt;

}

