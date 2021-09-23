package com.wisdom.auth.config.auth;

import com.wisdom.auth.service.SysUserServiceExt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.annotation.Resource;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * TODO (用一句话描述该类的作用)
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/13 14:53 星期一
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${spring.application.name:auth}")
    private String appName;

    @Resource
    private SysUserServiceExt sysUserServiceExt;

    /**
     * 配置登录信息
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
//        http.formLogin().and().httpBasic();
        http.formLogin().loginPage("/auth/page").failureHandler((request, response, exception) -> System.out.println("failed"))
                .successHandler((request, response, authentication) -> System.out.println("success"));
    }

    /**
     * 配置用户登录验证服务
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(sysUserServiceExt);
    }

    /**
     * 配置放开路径
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/" + appName + "/login", "/" + appName + "/authorize", "/" + appName + "/check_token", "/" + appName + "/refresh_token", "/" + appName + "/druid/**");
    }

}