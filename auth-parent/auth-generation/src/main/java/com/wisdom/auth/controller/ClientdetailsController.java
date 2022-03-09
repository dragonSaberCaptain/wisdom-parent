package com.wisdom.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisdom.auth.service.ClientdetailsService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import com.wisdom.common.controller.BaseController;
import com.wisdom.auth.entity.Clientdetails;
/**
 *  控制层
 *
 * @author captain
 * @version 1.0.0
 */
@RestController
@RequestMapping("/auth/clientdetails")
@Api(value = "ClientdetailsController API", tags = "clientdetails: 相关接口")
public class ClientdetailsController extends BaseController<ClientdetailsService, Clientdetails> {

}

