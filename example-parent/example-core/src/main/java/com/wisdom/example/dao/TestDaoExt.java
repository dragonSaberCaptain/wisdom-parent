package com.wisdom.example.dao;

import com.wisdom.example.entity.TestExt;
import com.wisdom.core.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 测试表 持久层
 *
 * @author captain
 * @version 1.0.0
 */
@Mapper
public interface TestDaoExt extends BaseDao<TestExt> {

}
