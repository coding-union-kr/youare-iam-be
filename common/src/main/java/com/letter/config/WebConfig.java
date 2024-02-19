package com.letter.config;

import com.letter.interceptor.LoginCheckInterceptor;
import com.letter.interceptor.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginCheckInterceptor loginCheckInterceptor;
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    @Value("${origin.frontend.prod}")
    private String frontendProdOrigin;
    @Value("${origin.bambam}")
    private String bambamOrigin;
    @Value("${origin.biyamn}")
    private String biyamnOrigin;

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        // TODO swagger는 모든 Origin에 열어놓기
        corsRegistry.addMapping("/**")
                .allowedOrigins(frontendProdOrigin, bambamOrigin, biyamnOrigin)
                .allowedMethods("*")
//                .allowedHeaders("Access-Control-Request-Headers") // 클라이언트에서 헤더에서 접근 가능하게 하기 위한 설정
                .allowedHeaders("*")
                .exposedHeaders("Authorization") // 나가는 헤더 설정
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * Controller method를 실행하기 전에 LoginCheckInterceptor를 실행하게 된다.
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/**") // 모든 패턴 가능
                .excludePathPatterns("/swagger-ui/index.html") // 스웨거 쪽 제외
                .excludePathPatterns("/api-docs") // 스웨거 쪽 제외
                .excludePathPatterns("/api/v1/members/kakao/callback"); // 로그인 제외

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }
}
