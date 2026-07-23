package com.kanban.kanbanbackend.config;

import com.kanban.kanbanbackend.service.CustomUserDetailsService;
import com.kanban.kanbanbackend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override

    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request, response);
            return;
        }
        String path = request.getServletPath();

        // Skip JWT for Swagger endpoints
        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("========== JWT FILTER ==========");

        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header = " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("No Bearer Token Found");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        System.out.println("Token = " + token);

        boolean valid = jwtService.isTokenValid(token);
        System.out.println("Token Valid = " + valid);

        if (!valid) {
            System.out.println("Invalid Token");
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtService.extractEmail(token);
        System.out.println("Email = " + email);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        System.out.println("User Loaded = " + userDetails.getUsername());

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println("Authentication Success");
        System.out.println(request.getMethod());
        System.out.println(request.getRequestURI());

        filterChain.doFilter(request, response);
    }
}