package com.sparta.miniproject.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // CORS를 적용할 URL 패턴을 정의
                .allowedOrigins("*") // 자원 공유를 허락할 Origin을 지정
                .allowedMethods("*") // 모든 Http Method 허용
                .maxAge(3000); // 원하는 시간만큼 pre-flight 리퀘스트를 캐싱 해둘 수 있다.
    }
}
