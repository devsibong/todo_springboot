package com.devsibong.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

import com.devsibong.demo.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors().and().csrf().disable() // CORS 설정과 CSRF 보호 비활성화
            .httpBasic().disable() // HTTP Basic 인증 비활성화
            .authorizeRequests()
                .antMatchers("/", "/auth/**").permitAll() // "/"와 "/auth/**" 경로는 모든 사용자에게 허용
                .anyRequest().authenticated() // 그 이외의 모든 요청은 인증 필요
            .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않음 (Stateless 인증)
            .and()
                .addFilterAfter(jwtAuthenticationFilter, CorsFilter.class); // JwtAuthenticationFilter를 CorsFilter 다음에 추가
        return http.build();
    }
}