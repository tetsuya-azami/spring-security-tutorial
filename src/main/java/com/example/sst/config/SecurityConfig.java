package com.example.sst.config;

import com.example.sst.exception.handler.ApplicationAccessDeniedHandler;
import com.example.sst.exception.handler.ApplicationAuthenticationEntryPoint;
import com.example.sst.filter.JWTTokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final ApplicationAuthenticationEntryPoint authenticationEntryPoint;
    private final ApplicationAccessDeniedHandler accessDeniedHandler;

    //    private final OpaqueTokenAuthenticationFilter opaqueTokenAuthenticationFilter;
    private final JWTTokenAuthenticationFilter jwtTokenAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.addFilterBefore(jwtTokenAuthenticationFilter, AuthorizationFilter.class)
                .authorizeHttpRequests(
                        matcher -> matcher
                                .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/auth/loginWithJWT").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(
                        configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(
                        customizer -> customizer
                                .authenticationEntryPoint(authenticationEntryPoint)
                                .accessDeniedHandler(accessDeniedHandler));

        return http.build();
    }
}
