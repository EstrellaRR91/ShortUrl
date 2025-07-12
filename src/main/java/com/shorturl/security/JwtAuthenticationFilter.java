package com.shorturl.security;

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

import org.springframework.lang.NonNull;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    @Autowired
    private JwUt jwtUtil;

    @Autowired
    
    private CustomUserDetailsService userDetailsService;

    
    @Override
    protected void doFilterInternal (
    @NonNull HttpServletRequest request,
    @NonNull HttpServletResponse response, 
    @NonNull FilterChain filterChain )

    throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer")){
            filterChain.doFilter(request, response);
            return;

        }
        
        final String token= authHeader.substring(7);
        final String username = jwtUtil.getUserNameFromJwtToken(token);


        if (username != null && SecurityContextHolder.getContext().getAuthentication()== null){
            UserDetails userDetails = userDetailsService.loadUserByUsername (username); 

            if( jwtUtil.validateJwtToken(token)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        
    
        filterChain.doFilter(request, response);

    }
    
}
