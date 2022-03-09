package com.wisdom.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisdom.auth.service.OauthAccessTokenService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import com.wisdom.common.controller.BaseController;
import com.wisdom.auth.entity.OauthAccessToken;
/**
 *  控制层
 *
 * @author captain
 * @version 1.0.0
 */
@RestController
@RequestMapping("/auth/oauthAccessToken")
@Api(value = "OauthAccessTokenController API", tags = "oauth_access_token: 相关接口")
public class OauthAccessTokenController extends BaseController<OauthAccessTokenService, OauthAccessToken> {

}

