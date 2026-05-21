package com.mahmoud.devCollab.filter;

import com.mahmoud.devCollab.security.CustomUserDetails;
import com.mahmoud.devCollab.security.jwt.Jwt;
import com.mahmoud.devCollab.security.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.replace("Bearer ", "");

        Jwt jwt = jwtService.parseToken(token);

        if (jwt == null || jwt.isExpired()) {
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContext auth = SecurityContextHolder.createEmptyContext();

        CustomUserDetails userDetails = new CustomUserDetails(
            jwt.getUserId(),
            jwt.getUsername(),
            jwt.getEmail(),
            jwt.getRole()
        );

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        var authentication = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            authorities
        );

        authentication.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
        );

        auth.setAuthentication(authentication);

        SecurityContextHolder.setContext(auth);

        filterChain.doFilter(request, response);
    }
}
