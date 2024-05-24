package dmu.cheek.global.config.security;

import dmu.cheek.global.config.token.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.access-token-validity-in-seconds}")
    private long accessTokenExpirationTime;
    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpirationTime;
    @Value("${jwt.secret}")
    private String tokenSecret;

    @Bean
    public TokenManager tokenManager() {
        return new TokenManager(accessTokenExpirationTime, refreshTokenExpirationTime, tokenSecret);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(CsrfConfigurer::disable) //CSRF 보호 비활성화
                .formLogin(AbstractHttpConfigurer::disable) //폼 로그인 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) //HTTP 기본 인증 비활성화
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션 무상태
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/register", "/", "/login").permitAll()
                        .anyRequest().authenticated())
                //TODO: jwt 구현 후 삭제하기
                .logout((logout) -> logout
                        .logoutSuccessUrl("/") //로그아웃 시 메인 화면으로
                        .invalidateHttpSession(true) //http 세션 무효화
                );

        return httpSecurity.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
