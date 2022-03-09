package com.wisdom.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisdom.auth.service.OauthClientDetailsService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import com.wisdom.common.controller.BaseController;
import com.wisdom.auth.entity.OauthClientDetails;
/**
 *  控制层
 *
 * @author captain
 * @version 1.0.0
 */
@RestController
@RequestMapping("/auth/oauthClientDetails")
@Api(value = "OauthClientDetailsController API", tags = "oauth_client_details: 相关接口")
public class OauthClientDetailsController extends BaseController<OauthClientDetailsService, OauthClientDetails> {

}

