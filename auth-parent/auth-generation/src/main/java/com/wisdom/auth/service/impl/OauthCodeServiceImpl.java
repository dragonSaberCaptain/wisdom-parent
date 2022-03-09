package com.wisdom.auth.service.impl;

import com.wisdom.auth.entity.OauthCode;
import com.wisdom.auth.dao.OauthCodeDao;
import com.wisdom.auth.service.OauthCodeService;
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
public class OauthCodeServiceImpl extends BaseServiceImpl<OauthCodeDao, OauthCode> implements OauthCodeService {

}
