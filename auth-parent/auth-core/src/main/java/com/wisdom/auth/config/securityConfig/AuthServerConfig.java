package com.wisdom.auth.config.securityConfig;

import com.wisdom.auth.service.SysUserServiceExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * TODO (用一句话描述该类的作用)
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/11/25 14:33 星期四
 */
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
	@Autowired
	private DataSource dataSource;
	/**
	 * 注入用于支持 password 模式
	 */
	@Autowired
	private AuthenticationManager authenticationManager;

	/**
	 * 该对象为刷新token提供支持
	 */
	@Lazy
	@Autowired
	private SysUserServiceExt sysUserServiceExt;

	/**
	 * 设置用户密码的加密方式
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	/**
	 * Token 令牌保存到数据库
	 */
	@Bean
	public TokenStore tokenStoreToDb() {
		return new JdbcTokenStore(dataSource);
	}

	/**
	 * Token 令牌保存到缓存中
	 */
	@Bean
	public TokenStore tokenStoreToCache() {
		return new JdbcTokenStore(dataSource);
	}

	/**
	 * A service that provides the details about an OAuth2 client.
	 *
	 * @return ClientDetailsService
	 * <p>
	 * 基于 JDBC 实现，需要事先在数据库配置客户端信息
	 */
	@Bean
	public ClientDetailsService jdbcClientDetailsService() {
		return new JdbcClientDetailsService(dataSource);
	}

	/**
	 * Authorization Server endpoints.
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStoreToDb())
				.userDetailsService(sysUserServiceExt)
				.authorizationCodeServices(authorizationCodeServices())
				.authenticationManager(authenticationManager); // 用于支持密码模式

		// 配置tokenServices参数
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setTokenStore(endpoints.getTokenStore());
		tokenServices.setSupportRefreshToken(false);
		tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
		tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
		tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30)); // 30天
		endpoints.tokenServices(tokenServices);

	}

	/**
	 * 授权服务安全配置，permitAll() 表示对于端点可以匿名访问如下
	 * [/oauth/authorize] [/oauth/token] [/oauth/check_token]
	 * [/oauth/confirm_access] [/oauth/token_key] [/oauth/error]
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.allowFormAuthenticationForClients()
				.passwordEncoder(passwordEncoder()) //密码模式的加密方式
				.tokenKeyAccess("permitAll()")
				.checkTokenAccess("isAuthenticated()");
	}

	/**
	 * 配置 oauth_client_details【client_id和client_secret等】信息的认证【检查ClientDetails的合法性】服务
	 * 设置 认证信息的来源：数据库 (可选项：数据库和内存,使用内存一般用来作测试)
	 * 自动注入：ClientDetailsService 的实现类 JdbcClientDetailsService (检查 ClientDetails 对象)
	 * 1.inMemory 方式存储的，将配置保存到内存中，相当于硬编码了。正式环境下的做法是持久化到数据库中，比如mysql 中。
	 * 2. secret加密是client_id:secret 然后通过base64编码后的字符串
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(jdbcClientDetailsService());
	}

	@Bean
	public AuthorizationCodeServices authorizationCodeServices() {
		return new JdbcAuthorizationCodeServices(dataSource);
	}
}