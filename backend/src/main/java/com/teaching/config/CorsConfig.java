package com.teaching.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/**")
                // 开发阶段允许所有来源；生产环境应改为具体域名
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                // allowCredentials=true 时不能同时使用 allowedOrigins("*")
                // Spring 5.3+ 使用 allowedOriginPatterns("*") 自动回显具体 Origin，规避此限制
                .allowCredentials(true)
                .maxAge(3600);
    }
}
