package com.wisdom.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisdom.auth.service.OauthRefreshTokenService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import com.wisdom.common.controller.BaseController;
import com.wisdom.auth.entity.OauthRefreshToken;
/**
 *  控制层
 *
 * @author captain
 * @version 1.0.0
 */
@RestController
@RequestMapping("/auth/oauthRefreshToken")
@Api(value = "OauthRefreshTokenController API", tags = "oauth_refresh_token: 相关接口")
public class OauthRefreshTokenController extends BaseController<OauthRefreshTokenService, OauthRefreshToken> {

}

