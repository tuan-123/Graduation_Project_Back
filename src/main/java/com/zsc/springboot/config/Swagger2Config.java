package com.zsc.springboot.config;


import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 *  swagger2 配置
 *@Author：黄港团
 *@Since：2021/1/12 16:38
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zsc.springboot.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("CampusHelper_API")
                .description("接口文档")
                .termsOfServiceUrl("http://localhost:8087")
                .version("2.3.1")
                .build();
    }
}
