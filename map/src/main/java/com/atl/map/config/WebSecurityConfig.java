package com.atl.map.config;

import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.atl.map.filter.JwtAuthenticationFilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configurable
@Configuration // Spring의 구성 클래스를 선언합니다.
@EnableWebSecurity // Spring Security를 활성화합니다.
@RequiredArgsConstructor // Lombok을 사용하여 final 필드를 인자로 하는 생성자를 자동으로 생성합니다.
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // JWT 인증 필터를 주입받습니다.

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource())) // CORS 설정을 정의합니다.
                .csrf(CsrfConfigurer::disable) // CSRF 보호를 비활성화합니다.
                .httpBasic(HttpBasicConfigurer::disable) // 기본 HTTP 인증을 비활성화합니다.
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션을 STATELESS로 설정하여 세션을 사용하지 않게 합니다.
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/", "/api/v1/auth/**", "/api/v1/user/{email}", 
                        "/file/**", "/api/v1/post/**", "/api/v1/marker/building/{buildingId}").permitAll() // 특정 경로는 인증 없이 접근을 허용합니다.
                        .requestMatchers("/api/v1/user/**").hasAuthority("ROLE_USER") // USER 역할을 가진 사용자만 접근 가능한 경로를 설정합니다.
                        .requestMatchers("/api/v1/admin/**").hasAuthority("ROLE_ADMIN") // ADMIN 역할을 가진 사용자만 접근 가능한 경로를 설정합니다.
                        .anyRequest().authenticated()) // 그 외의 모든 요청은 인증을 요구합니다.
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new FailedAuthenticationEntryPoint())) // 인증 실패 시 처리할 엔트리 포인트를 설정합니다.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // JWT 인증 필터를
                                                                                                       // UsernamePasswordAuthenticationFilter
                                                                                                       // 전에 추가합니다.

        return httpSecurity.build();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        corsConfiguration.addAllowedOrigin("*"); // 모든 도메인에서의 요청을 허용합니다.
        corsConfiguration.addAllowedMethod("*"); // 모든 HTTP 메소드를 허용합니다.
        corsConfiguration.addAllowedHeader("*"); // 모든 헤더를 허용합니다.

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/v1/**", corsConfiguration); // 모든 경로에 대해 CORS 설정을 적용합니다.

        return source;
    }
}

class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {
    // 인증 실패 시 클라이언트에게 응답할 로직을 구현합니다.
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden 응답 코드를 설정합니다.
        response.getWriter().write("{\"code\":\"NP\", \"message\": \"No Permission\"}"); // 에러 메시지를 JSON 형태로 응답합니다.
    }
}
