package dmu.cheek.global.config.security;

import dmu.cheek.global.config.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity
//                .httpBasic(Customizer.withDefaults())
//                .csrf(csrf -> csrf.disable()) //CSRF 보호 비활성화
//                .authorizeHttpRequests(auth -> auth.requestMatchers("/**").permitAll()) //모든 요청에 대해 인증 없이 접근 가능하도록 설정
//                .httpBasic(httpBasic -> httpBasic.disable()) //HTTP 기본 인증 비활성화
//                .formLogin(formLogin -> formLogin.disable()) //폼 로그인 비활성화
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("**/admin/**").hasRole("ADMIN")
//                        .requestMatchers("**/mentor/**").hasRole("MENTOR")
////                        .requestMatchers("**/mentee/**").hasRole("MENTEE")
//                        .anyRequest().authenticated())
//                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
//                .build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable()) //CSRF 보호 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")   //ADMIN 역할만 접근 허용
                        .requestMatchers("/mentor/**").hasRole("MENTOR") //MENTOR 역할만 접근 허용
                        .requestMatchers("/mentee/**").hasRole("MENTEE") //MENTEE 역할만 접근 허용
                        .anyRequest().permitAll()                   //나머지 요청은 인증 필요
                )
                .httpBasic(Customizer.withDefaults()) //HTTP 기본 인증 사용
                .formLogin(formLogin -> formLogin.disable()) //폼 로그인 비활성화
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


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

        return authManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
