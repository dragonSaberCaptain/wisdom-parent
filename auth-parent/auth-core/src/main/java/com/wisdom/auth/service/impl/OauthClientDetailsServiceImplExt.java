package com.wisdom.auth.service.impl;

import com.wisdom.auth.dao.OauthClientDetailsDaoExt;
import com.wisdom.auth.entity.OauthClientDetailsExt;
import com.wisdom.auth.service.OauthClientDetailsServiceExt;
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
public class OauthClientDetailsServiceImplExt extends BaseServiceImpl<OauthClientDetailsDaoExt, OauthClientDetailsExt> implements OauthClientDetailsServiceExt {
    @Autowired
    private OauthClientDetailsDaoExt oauthClientDetailsDaoExt;

}
