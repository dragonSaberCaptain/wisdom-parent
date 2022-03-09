package com.wisdom.auth.service.impl;

import com.wisdom.auth.entity.OauthCodeExt;
import com.wisdom.auth.dao.OauthCodeDaoExt;
import com.wisdom.auth.service.OauthCodeServiceExt;
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
public class OauthCodeServiceImplExt extends BaseServiceImpl<OauthCodeDaoExt, OauthCodeExt> implements OauthCodeServiceExt {
    @Autowired
    private OauthCodeDaoExt oauthCodeDaoExt;

}
