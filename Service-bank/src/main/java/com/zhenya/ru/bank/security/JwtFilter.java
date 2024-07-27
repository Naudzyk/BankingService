package com.zhenya.ru.bank.security;

import com.auth0.jwt.exceptions.JWTVerificationException;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
            String username = null;
            UserDetails userDetails = null;
        UsernamePasswordAuthenticationToken authenticationToken= null;
        try{
            String headerAuth = request.getHeader("Authorization");
            if(headerAuth!= null && headerAuth.startsWith("Bearer ")){
                token = headerAuth.substring(7);
            }
            if(token != null){
                try{
                    username = jwtUtil.getNameFromJwt(token);
                }catch (ExpiredJwtException e){

                }
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    userDetails =userDetailsService.loadUserByUsername(username);
                    authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                            userDetails.getPassword(),userDetails.getAuthorities()
                    );
                }
                if(SecurityContextHolder.getContext().getAuthentication() == null){
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }catch (JWTVerificationException e){


        }
        filterChain.doFilter(request,response);
    }
}
