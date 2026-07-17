package com.ecommerce.adapter.security.jwt;

import com.ecommerce.domain.auth.dto.IssuedToken;
import com.ecommerce.domain.auth.enums.TokenType;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    public static final String JWT_ERROR = "jwt.error";

    public enum JwtError {EXPIRED, INVALID,}

    private final JwtTokenProvider jwtTokenProvider;
    private final String accessCookieName;

    public JwtAuthFilter(JwtTokenProvider jwtTokenProvider,
                         @Value("${spring.jwt.cookie.access-name}") String accessCookieName) {

        this.jwtTokenProvider = jwtTokenProvider;
        this.accessCookieName = accessCookieName;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = resolveCookie(request);
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                IssuedToken issuedToken = jwtTokenProvider.parseToken(token);
                if (issuedToken.tokenType().equals(TokenType.ACCESS)) {
                    List<SimpleGrantedAuthority> authorities = issuedToken.roles().stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                            .toList();

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(issuedToken.id(), null, authorities);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }

            } catch (ExpiredJwtException e) {
                log.info("access token expired");
                request.setAttribute(JWT_ERROR, JwtError.EXPIRED);
            } catch (JwtException e) {
                log.info("Invalid jwt");
                request.setAttribute(JWT_ERROR, JwtError.INVALID);

            }


        }

        filterChain.doFilter(request, response);
    }

    private String resolveCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (accessCookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
