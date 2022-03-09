package com.wisdom.auth.service.impl;

import com.wisdom.auth.entity.OauthAccessToken;
import com.wisdom.auth.dao.OauthAccessTokenDao;
import com.wisdom.auth.service.OauthAccessTokenService;
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
public class OauthAccessTokenServiceImpl extends BaseServiceImpl<OauthAccessTokenDao, OauthAccessToken> implements OauthAccessTokenService {

}
