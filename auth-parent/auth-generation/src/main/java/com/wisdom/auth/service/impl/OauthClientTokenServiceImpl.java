package com.wisdom.auth.service.impl;

import com.wisdom.auth.entity.OauthClientToken;
import com.wisdom.auth.dao.OauthClientTokenDao;
import com.wisdom.auth.service.OauthClientTokenService;
import com.wisdom.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  逻辑层
 *
 * @author captain
 * @version 1.0.0
 */
@Service
public class OauthClientTokenServiceImpl extends BaseServiceImpl<OauthClientTokenDao, OauthClientToken> implements OauthClientTokenService {

}
