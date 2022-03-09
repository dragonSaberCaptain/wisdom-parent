package com.wisdom.auth.config.securityConfig;

import com.wisdom.auth.service.impl.SysUserServiceImplExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @Description: 身份认证拦截
 * @EnableAuthorizationServer 该注解用来开启认证服务，使用该注解表明自己是一个认证服务。
 * @EnableResourceServer 该注解要用来开启资源保护，表明自己是资源服务器受认证服务保护。
 * @EnableOAuth2Sso 该注解表示自己是oauth2客户端，也即单点登录客户端
 */
@Configuration
@EnableWebSecurity
//注解权限拦截 判断用户对某个控制层的方法是否具有访问权限
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SysUserServiceImplExt sysUserServiceImplExt;

    /**
     * 配置用户登录验证服务
     *
     * @author admin
     * @datetime 2022-02-08 10:13:59
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(sysUserServiceImplExt);
    }
}