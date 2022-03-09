package com.wisdom.auth.dao;

import com.wisdom.auth.entity.OauthRefreshToken;
import com.wisdom.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 *  持久层
 *
 * @author captain
 * @version 1.0.0
 */
@Mapper
public interface OauthRefreshTokenDao extends BaseDao<OauthRefreshToken> {

}
