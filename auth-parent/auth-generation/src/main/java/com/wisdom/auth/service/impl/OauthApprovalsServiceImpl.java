package com.wisdom.auth.service.impl;

import com.wisdom.auth.entity.OauthApprovals;
import com.wisdom.auth.dao.OauthApprovalsDao;
import com.wisdom.auth.service.OauthApprovalsService;
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
public class OauthApprovalsServiceImpl extends BaseServiceImpl<OauthApprovalsDao, OauthApprovals> implements OauthApprovalsService {

}
