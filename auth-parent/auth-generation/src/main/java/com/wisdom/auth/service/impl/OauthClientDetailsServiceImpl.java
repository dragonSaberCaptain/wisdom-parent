package com.wisdom.auth.service.impl;

import com.wisdom.auth.entity.OauthClientDetails;
import com.wisdom.auth.dao.OauthClientDetailsDao;
import com.wisdom.auth.service.OauthClientDetailsService;
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
public class OauthClientDetailsServiceImpl extends BaseServiceImpl<OauthClientDetailsDao, OauthClientDetails> implements OauthClientDetailsService {

}
