package com.wisdom.auth.service.impl;

import com.wisdom.auth.entity.Clientdetails;
import com.wisdom.auth.dao.ClientdetailsDao;
import com.wisdom.auth.service.ClientdetailsService;
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
public class ClientdetailsServiceImpl extends BaseServiceImpl<ClientdetailsDao, Clientdetails> implements ClientdetailsService {

}
