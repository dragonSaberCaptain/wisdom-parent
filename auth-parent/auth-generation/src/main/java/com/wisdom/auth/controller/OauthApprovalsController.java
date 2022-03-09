package com.wisdom.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisdom.auth.service.OauthApprovalsService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import com.wisdom.common.controller.BaseController;
import com.wisdom.auth.entity.OauthApprovals;
/**
 *  控制层
 *
 * @author captain
 * @version 1.0.0
 */
@RestController
@RequestMapping("/auth/oauthApprovals")
@Api(value = "OauthApprovalsController API", tags = "oauth_approvals: 相关接口")
public class OauthApprovalsController extends BaseController<OauthApprovalsService, OauthApprovals> {

}

