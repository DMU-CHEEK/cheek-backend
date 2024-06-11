package dmu.cheek.global.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable()) //CSRF 보호 비활성화
                .authorizeHttpRequests(auth -> auth.requestMatchers("/**").permitAll()) //모든 요청에 대해 인증 없이 접근 가능하도록 설정
                .httpBasic(httpBasic -> httpBasic.disable()) //HTTP 기본 인증 비활성화
                .formLogin(formLogin -> formLogin.disable()) //폼 로그인 비활성화
                .build();
    }

}
