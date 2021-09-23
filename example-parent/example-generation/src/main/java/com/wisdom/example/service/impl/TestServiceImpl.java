package com.wisdom.example.service.impl;

import com.wisdom.example.entity.Test;
import com.wisdom.example.dao.TestDao;
import com.wisdom.example.service.TestService;
import com.wisdom.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * Copyright ©2021 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 测试表 逻辑层
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2021-09-23 09:43:03 星期四
 */
@Slf4j
@Service
public class TestServiceImpl extends BaseServiceImpl<TestDao, Test> implements TestService {
    @Resource
    private TestDao testDao;

}
