package com.wisdom.common.config.swagger;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.*;

/**
 * Copyright © 2018 dragonSaberCaptain. All rights reserved.
 * <p>
 * http://localhost:post/swagger-ui/index.html
 * http://localhost:post/doc.html
 * <p>
 *
 * @author dragonSaberCaptain
 * @date 2018-05-03 10:16 星期四
 */

@EnableOpenApi
@Configuration
public class SwaggerConfig {
    @Value("${spring.application.name:}")
    private String appName = "dragonSaberCaptain";

    @Value("${common.params.token_key:token}")
    private String token;

    @Value("${server.port}")
    private String port = "80";

    @Bean
    public Docket createRestApi() {
        //设置全局响应状态码
        List<Response> responseList = new ArrayList<>();
//        for (HttpEnum value : HttpEnum.values()) {
//            String message = value.getMsg();
//            String subMsg = value.getSubMsg();
//            if (StringUtil.isNotBlank(subMsg)) {
//                message = value.getMsg() + " " + value.getSubMsg();
//            }
//            responseList.add(new ResponseBuilder().code(value.getCode()).description(message).build());
//        }
//
//        for (ResultEnum value : ResultEnum.values()) {
//            String message = value.getMsg();
//            String subMsg = value.getSubMsg();
//            if (StringUtil.isNotBlank(subMsg)) {
//                message = value.getMsg() + " " + value.getSubMsg();
//            }
//            responseList.add(new ResponseBuilder().code(value.getCode()).description(message).build());
//        }

        return new Docket(DocumentationType.OAS_30)
                .pathMapping("/")
                // 定义是否开启swagger，false为关闭，可以通过变量控制
                .enable(true)
                // 将api的元信息设置为包含在json ResourceListing响应中。
                .apiInfo(apiInfo())
                .globalResponses(HttpMethod.GET, responseList)
                .globalResponses(HttpMethod.HEAD, responseList)
                .globalResponses(HttpMethod.POST, responseList)
                .globalResponses(HttpMethod.PUT, responseList)
                .globalResponses(HttpMethod.PATCH, responseList)
                .globalResponses(HttpMethod.DELETE, responseList)
                .globalResponses(HttpMethod.OPTIONS, responseList)
                .globalResponses(HttpMethod.TRACE, responseList)
                // 接口调试地址
//                .host("http://localhost:" + port)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .paths(PathSelectors.regex("/" + appName + "/error").negate())
                .paths(PathSelectors.regex("/oauth.*").negate())
                .paths(PathSelectors.regex("/swagger-resources").negate())
                .paths(PathSelectors.regex("/error").negate())

                .build()
                // 支持的通讯协议集合
                .protocols(newHashSet("https", "http"))
                // 授权信息设置，必要的header token等认证信息
                .securitySchemes(securitySchemes())
                // 授权信息全局应用
                .securityContexts(securityContexts());
    }


    /**
     * API 页面上半部分展示信息
     */
    private ApiInfo apiInfo() {
        String url = "https://www." + appName + ":" + port + ".com/";
        return new ApiInfoBuilder()
                //大标题
                .title(appName + ":" + port + " Api Doc")
                //详细描述
                .description(appName + ":" + port + " exchange platform for API")
                //联系信息
                .contact(new Contact("dragonSaberCaptain", url, "749969369@qq.com"))
                //版本
                .version("Application Version: 1.0, Spring Boot Version: " + SpringBootVersion.getVersion())
                //服务条款网址
                .termsOfServiceUrl(url)
                //Apache许可证，版本2.0
                .license("The Apache License, Version 2.0")
                //Apache许可证Url
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .build();
    }


    /**
     * 设置授权信息
     */
    private List<SecurityScheme> securitySchemes() {
        ApiKey apiKey = new ApiKey(token, "token", In.HEADER.toValue());
        return Collections.singletonList(apiKey);
    }


    /**
     * 授权信息全局应用
     */
    private List<SecurityContext> securityContexts() {
        return Collections.singletonList(
                SecurityContext.builder()
                        .securityReferences(Collections.singletonList(new SecurityReference(token, new AuthorizationScope[]{new AuthorizationScope("global", "")})))
                        .build()
        );
    }


    @SafeVarargs
    private final <T> Set<T> newHashSet(T... ts) {
        if (ts.length > 0) {
            return new LinkedHashSet<>(Arrays.asList(ts));
        }
        return null;
    }
}
