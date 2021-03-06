package com.wisdom.common.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * TODO (用一句话描述该类的作用)
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/15 18:07 星期三
 */
@Slf4j
@Configuration
public class ApiAdapter extends WebMvcConfigurationSupport {

    /**
     * 处理拦截器先于springIOC执行，导致ApiInterceptor中属性注入失败问题
     *
     * @author captain
     * @datetime 2021-09-24 14:27:46
     */
    @Bean
    public ApiInterceptor apiInterceptor() {
        return new ApiInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链 addPathPatterns 用于添加拦截规则 excludePathPatterns 用户排除拦截
        registry.addInterceptor(apiInterceptor()).addPathPatterns("/**");
//            .excludePathPatterns("/swagger","/api-docs");
        super.addInterceptors(registry);
    }
}
