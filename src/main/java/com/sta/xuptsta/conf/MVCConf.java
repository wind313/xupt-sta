package com.sta.xuptsta.conf;

import com.sta.xuptsta.interceptor.AuthInterceptor;
import com.sta.xuptsta.interceptor.EmailLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCConf implements WebMvcConfigurer {
    @Autowired
    AuthInterceptor authInterceptor;

    @Autowired
    EmailLimitInterceptor emailLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(emailLimitInterceptor).addPathPatterns("/email/**");
    }
}
