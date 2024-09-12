package dmu.cheek.global.config.security;

import dmu.cheek.global.config.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

//    @Bean
//    public CustomUserDetailsService customUserDetailsService() {
//        return customUserDetailsService();
//    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable()) //CSRF 보호 비활성화
                .authorizeHttpRequests(auth -> auth.requestMatchers("/**").permitAll()) //모든 요청에 대해 인증 없이 접근 가능하도록 설정
                .httpBasic(httpBasic -> httpBasic.disable()) //HTTP 기본 인증 비활성화
                .formLogin(formLogin -> formLogin.disable()) //폼 로그인 비활성화
                .authorizeHttpRequests(auth -> auth
//                        .mat("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/story/**").hasRole("MENTOR")
//                        .antMatchers("/mentee/**").hasRole("MENTEE")
                        .anyRequest().authenticated())

                .build();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_MENTOR > ROLE_MENTEE");

        return roleHierarchy;
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }

}
