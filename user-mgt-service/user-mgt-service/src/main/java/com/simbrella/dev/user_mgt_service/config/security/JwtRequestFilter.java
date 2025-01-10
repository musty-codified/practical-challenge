package com.simbrella.dev.user_mgt_service.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simbrella.dev.user_mgt_service.dto.response.ErrorResponse;
import com.simbrella.dev.user_mgt_service.util.LocalStorage;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@org.jetbrains.annotations.NotNull HttpServletRequest request, @org.jetbrains.annotations.NotNull HttpServletResponse response, @org.jetbrains.annotations.NotNull FilterChain filterChain) throws ServletException, IOException {
        try {

            SecurityContext context = SecurityContextHolder.getContext();
            if (context.getAuthentication() != null && context.getAuthentication().isAuthenticated()) {
                filterChain.doFilter(request, response);
                return;
            }

            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = authorization.substring("Bearer ".length());
            String usernameFromToken = jwtUtil.getUsernameFromToken(token);
            UserDetails detailsFromToken = userDetailsService.loadUserByUsername(usernameFromToken);
                for (GrantedAuthority authority : detailsFromToken.getAuthorities()){
                    log.info("authority claims:{}", authority);

                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usernameFromToken, null, detailsFromToken.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token", e);
            sendError(response, "Please log in again");
        } catch (Exception e) {
            log.info("Exception occurred while filtering");
            log.error(e.getMessage(), e);
            sendError(response, "An unknown error occurred during authentication");
        }
    }

    private void sendError(@NotNull HttpServletResponse response, String message) throws IOException {
        ErrorResponse body = new ErrorResponse(message);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        writer.print(new ObjectMapper().writeValueAsString(body));
        writer.flush();
        writer.close();
    }

}
