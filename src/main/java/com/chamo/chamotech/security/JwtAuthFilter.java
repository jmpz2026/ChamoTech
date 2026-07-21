package com.chamo.chamotech.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, jakarta.servlet.ServletException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Authorization header is missing\"}");
            return;
        }

        String token = authHeader.substring("Bearer ".length());

        try {
            if (jwtService.isTokenValid(token)) {
                request.setAttribute("username", jwtService.extractUsername(token));
                request.setAttribute("userId", jwtService.extractUserId(token));
                request.setAttribute("role", jwtService.extractRole(token));
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token is invalid or expired\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token validation failed\"}");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        if (PublicRoutes.PUBLIC_ROUTES.stream().anyMatch(path::startsWith)) {
            return true;
        }

        boolean isGet = "GET".equalsIgnoreCase(request.getMethod());
        return isGet && PublicRoutes.PUBLIC_GET_ROUTES.stream().anyMatch(path::startsWith);
    }
}
