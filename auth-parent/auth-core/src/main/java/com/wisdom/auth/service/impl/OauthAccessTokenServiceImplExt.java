package com.wisdom.auth.service.impl;

import com.wisdom.auth.dao.OauthAccessTokenDaoExt;
import com.wisdom.auth.entity.OauthAccessTokenExt;
import com.wisdom.auth.service.OauthAccessTokenServiceExt;
import com.wisdom.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *  逻辑层
 *
 * @author captain
 * @version 1.0.0
 */
@Slf4j
@Service
public class OauthAccessTokenServiceImplExt extends BaseServiceImpl<OauthAccessTokenDaoExt, OauthAccessTokenExt> implements OauthAccessTokenServiceExt {
    @Autowired
    private OauthAccessTokenDaoExt oauthAccessTokenDaoExt;

}
