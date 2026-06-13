package com.ecommerce.adapter.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    public static final String[] SECURITY_EXCLUDE_PATHS = {
            "/public/**", "/api/swagger-ui/**", "/swagger-ui/**", "/swagger-ui.html",
            "/api/v3/api-docs/**", "/v3/api-docs/**", "/favicon.ico", "/actuator/**",
            "/swagger-resources/**", "/external/**", "/api/auth/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(SECURITY_EXCLUDE_PATHS).permitAll()
                                .requestMatchers("/api/**").hasRole("USER")
                                .anyRequest().authenticated());


        return http.build();

    }
}
