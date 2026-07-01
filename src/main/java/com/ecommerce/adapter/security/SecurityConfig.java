package com.ecommerce.adapter.security;


import com.ecommerce.adapter.security.jwt.JwtAuthFilter;
import com.ecommerce.adapter.webApi.common.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    public static final String[] SECURITY_EXCLUDE_PATHS = {
            "/public/**", "/api/swagger-ui/**", "/swagger-ui/**", "/swagger-ui.html",
            "/api/v3/api-docs/**", "/v3/api-docs/**", "/favicon.ico", "/actuator/**",
            "/swagger-resources/**", "/external/**", "/api/auth/**"
    };

    private final ObjectMapper objectMapper;
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf((csrf) -> {
                    CsrfTokenRequestAttributeHandler handler = new CsrfTokenRequestAttributeHandler();
                    csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                            .csrfTokenRequestHandler(handler)
                            .ignoringRequestMatchers("/api/auth/**");
                })
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement((session) ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(SECURITY_EXCLUDE_PATHS).permitAll()
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .requestMatchers("/api/**").hasRole("USER")
                                .anyRequest().authenticated()
                ).exceptionHandling(exception ->
                        exception.authenticationEntryPoint(((request, response, authException) -> {
                            Object jwtError = request.getAttribute(JwtAuthFilter.JWT_ERROR);

                            String code = "UNAUTHORIZED";
                            String message = "auth failed";

                            if (jwtError == JwtAuthFilter.JwtError.EXPIRED) {
                                code = "TOKEN_EXPIRED";
                                message = "access token expired";
                                response.setHeader("X-Auth-Error", "token_expired");
                            } else if (jwtError == JwtAuthFilter.JwtError.INVALID) {
                                code = "INVALID_TOKEN";
                                message = "invalid token";
                            }

                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json;charset=UTF-8");

                            ApiResponse<Void> error = ApiResponse.<Void>builder()
                                    .error(ApiResponse.Error.of(code, message))
                                    .build();
                            response.getWriter().write(objectMapper.writeValueAsString(error));

                        })).accessDeniedHandler(((request, response, accessDeniedException) -> {

                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json;charset=UTF-8");
                            ApiResponse<Void> error = ApiResponse.<Void>builder()
                                    .error(ApiResponse.Error.of("FORBIDDEN", "access denied"))
                                    .build();
                            response.getWriter().write(objectMapper.writeValueAsString(error));

                        }))

                ).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
