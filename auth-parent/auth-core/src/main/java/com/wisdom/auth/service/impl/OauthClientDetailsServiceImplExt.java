package com.wisdom.auth.service.impl;

import com.wisdom.auth.entity.OauthClientDetailsExt;
import com.wisdom.auth.dao.OauthClientDetailsDaoExt;
import com.wisdom.auth.service.OauthClientDetailsServiceExt;
import com.wisdom.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

/**
 *  逻辑层
 *
 * @author captain
 * @version 1.0.0
 */
@Slf4j
@Service
public class OauthClientDetailsServiceImplExt extends BaseServiceImpl<OauthClientDetailsDaoExt, OauthClientDetailsExt> implements OauthClientDetailsServiceExt {
    @Autowired
    private OauthClientDetailsDaoExt oauthClientDetailsDaoExt;

}
