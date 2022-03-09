package com.wisdom.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisdom.auth.service.OauthClientTokenService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import com.wisdom.common.controller.BaseController;
import com.wisdom.auth.entity.OauthClientToken;
/**
 *  控制层
 *
 * @author captain
 * @version 1.0.0
 */
@RestController
@RequestMapping("/auth/oauthClientToken")
@Api(value = "OauthClientTokenController API", tags = "oauth_client_token: 相关接口")
public class OauthClientTokenController extends BaseController<OauthClientTokenService, OauthClientToken> {

}

