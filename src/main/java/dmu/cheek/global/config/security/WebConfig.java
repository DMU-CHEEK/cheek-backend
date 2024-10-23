package dmu.cheek.global.config.security;

import dmu.cheek.global.interceptor.AuthInterceptor;
import dmu.cheek.global.resolver.memberInfo.MemberInfoArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired @Lazy
    private AuthInterceptor authInterceptor;
    @Autowired
    private MemberInfoArgumentResolver memberInfoArgumentResolver;

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
        interceptorRegistry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/oauth/login",
                        "token/access-token/issue",
                        "/member/logout",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v2/api-docs",
                        "/v2/api-docs/**",
                        "/webjars/**"
                )
                .order(1);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberInfoArgumentResolver);
    }
}
