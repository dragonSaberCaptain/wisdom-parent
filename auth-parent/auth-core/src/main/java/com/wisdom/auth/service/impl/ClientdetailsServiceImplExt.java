package com.wisdom.auth.service.impl;

import com.wisdom.auth.entity.ClientdetailsExt;
import com.wisdom.auth.dao.ClientdetailsDaoExt;
import com.wisdom.auth.service.ClientdetailsServiceExt;
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
public class ClientdetailsServiceImplExt extends BaseServiceImpl<ClientdetailsDaoExt, ClientdetailsExt> implements ClientdetailsServiceExt {
    @Autowired
    private ClientdetailsDaoExt clientdetailsDaoExt;

}
