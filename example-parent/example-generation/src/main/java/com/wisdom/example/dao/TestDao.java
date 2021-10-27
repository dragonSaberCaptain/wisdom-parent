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
 * @datetime 2021-10-27 17:05:18 星期三
 */
@Mapper
public interface TestDao extends BaseDao<Test> {

}
