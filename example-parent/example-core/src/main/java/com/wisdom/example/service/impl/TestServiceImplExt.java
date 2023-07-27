package com.wisdom.example.service.impl;

import com.wisdom.example.entity.TestExt;
import com.wisdom.example.dao.TestDaoExt;
import com.wisdom.example.service.TestServiceExt;
import com.wisdom.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试表 逻辑层
 *
 * @author captain
 * @version 1.0.0
 */
@Slf4j
@Service
public class TestServiceImplExt extends BaseServiceImpl<TestDaoExt, TestExt> implements TestServiceExt {
    @Autowired
    private TestDaoExt testDaoExt;

}
