package dmu.cheek.global.config.security;

import dmu.cheek.global.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("dmu.cheek.*.controller")
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**") //모든 경로에 대해
                .allowedOrigins("http://localhost:8080") //요청을 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") //허용 메소드
                .allowedHeaders("*") //모든 헤더 허용
                .allowCredentials(true) //쿠키를 포함한 요청 허용
                .maxAge(3600); //pre-flight 요청의 캐시 시간(초 단위)
    }

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        interceptorRegistry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/member/login")
                .order(1);
    }
}
