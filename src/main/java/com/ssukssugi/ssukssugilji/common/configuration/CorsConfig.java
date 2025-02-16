package com.ssukssugi.ssukssugilji.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/api/**")  // 특정 API 경로에만 CORS 허용
//                    .allowedOrigins("http://localhost:8080") // 허용할 도메인
//                    .allowedMethods("GET", "POST", "PUT", "DELETE")
//                    .allowCredentials(true);
//            }
//        };
//    }
    
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // 모든 URL 허용
                    .allowedOrigins("*")
                    .allowedMethods("*");
            }
        };
    }
}
