package com.wisdom.example.dao;

import com.wisdom.example.entity.Test;
import com.wisdom.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * Copyright ©2021 dragonSaberCaptain inc. All rights reserved.
 *
 * <p>
 * 测试表 持久层
 * </p>
 *
 * @author captain
 * @version 1.0
 * @datetime 2021-12-02 13:22:05 星期四
 */
@Mapper
public interface TestDao extends BaseDao<Test> {

}
