package com.zsc.springboot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *  Cors
 *
 *@Author：黄港团
 *@Since：2021/1/12 21:08
 */

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Value("${zsc.uploadURL}")
    private String uploadURL;
    @Value("${zsc.userImg}")
    private String userImg;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //允许任何域名使用
                .allowedOrigins("*")
                //允许任何请求方法
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                //允许任何请求头
                .allowedHeaders("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + uploadURL);
        registry.addResourceHandler("/userImg/**").addResourceLocations("file:" + userImg);
    }
}
