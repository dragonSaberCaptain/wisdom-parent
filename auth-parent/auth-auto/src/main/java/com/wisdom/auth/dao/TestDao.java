package com.wisdom.auth.dao;

import com.wisdom.auth.entity.Test;
import com.wisdom.core.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 测试表 持久层
 *
 * @author captain
 * @version 1.0.0
 */
@Mapper
public interface TestDao extends BaseDao<Test> {

}
