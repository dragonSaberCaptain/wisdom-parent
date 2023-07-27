package com.wisdom.auth.service.impl;

import com.wisdom.auth.entity.Test;
import com.wisdom.auth.dao.TestDao;
import com.wisdom.auth.service.TestService;
import com.wisdom.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 测试表 逻辑层
 *
 * @author captain
 * @version 1.0.0
 */
@Service
public class TestServiceImpl extends BaseServiceImpl<TestDao, Test> implements TestService {

}
