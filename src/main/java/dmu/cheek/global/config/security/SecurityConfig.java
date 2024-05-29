package dmu.cheek.global.config.security;

import dmu.cheek.global.config.token.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
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

        return httpSecurity
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable()) //CSRF 보호 비활성화
                .authorizeHttpRequests(auth -> auth.requestMatchers("/**").permitAll()) // 모든 요청에 대해 인증 없이 접근 가능하도록 설정
                .httpBasic(httpBasic -> httpBasic.disable()) //HTTP 기본 인증 비활성화
                .formLogin(formLogin -> formLogin.disable()) //폼 로그인 비활성화
                .build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
