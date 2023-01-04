package com.yun.admin.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置
 *
 * @EnableAspectJAutoProxy(proxyTargetClass = true) 用来处理Service层直接使用类注入到Spring容器
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 跨域允许处理
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                //.allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                //.allowedHeaders("Content-Type","X-Request-With","Access-Control-Request-Method","Access-Control-Request-Headers","token");
                .allowedHeaders("*");
    }
}
