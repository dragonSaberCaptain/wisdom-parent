package com.wisdom.auth.config.securityConfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Copyright © 2022 dragonSaberCaptain. All rights reserved.
 * <p>
 * TODO (用一句话描述该类的作用)
 *
 * @author captain
 * @version 1.0
 * @datetime 2022/1/26 15:19 星期三
 */
@Configuration
@EnableSwagger2
public class WebApplicationConfig extends WebMvcConfigurationSupport {
    @Override
    protected void configureViewResolvers(ViewResolverRegistry registry) {
        registry.viewResolver(new InternalResourceViewResolver());
    }

}