package com.wisdom.auth.service.impl;

import com.wisdom.auth.dao.OauthClientTokenDaoExt;
import com.wisdom.auth.entity.OauthClientTokenExt;
import com.wisdom.auth.service.OauthClientTokenServiceExt;
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
public class OauthClientTokenServiceImplExt extends BaseServiceImpl<OauthClientTokenDaoExt, OauthClientTokenExt> implements OauthClientTokenServiceExt {
    @Autowired
    private OauthClientTokenDaoExt oauthClientTokenDaoExt;

}
