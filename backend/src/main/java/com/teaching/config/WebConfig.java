package com.teaching.config;

import com.teaching.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        final AuthInterceptor authInterceptor2 = authInterceptor;
        if (authInterceptor2 != null) {
            registry.addInterceptor(authInterceptor2)
                    .addPathPatterns("/api/**")
                    .excludePathPatterns("/api/login", "/api/test/**");
        } else {
            // TODO handle null value
        }
    }
}
