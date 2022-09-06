package com.sparta.miniproject.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**") // CORS를 적용할 URL 패턴을 정의
                .allowedOrigins("http://localhost:3000") // 자원 공유를 허락할 Origin을 지정
                .allowedMethods("*") // 모든 Http Method 허용
                .allowedHeaders("*")
                .exposedHeaders("*")
                .allowCredentials(true); // 내 서버가 응답할 떄 json을 자바스크립트에서 처리할 수 있게 할지를 설정하는 것. false로 하면 자바스크립트로 요청 했을 때 오지 않음.
    }
}
