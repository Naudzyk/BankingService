package com.zhenya.ru.bank.security;


import com.zhenya.ru.bank.service.impl.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtUtil {


    @Value("${jwt_secret}")
    private String secret;
    @Value("${jwt_lifetime}")
    private Duration lifetime;

    public String generateToken(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
         Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + lifetime.toMillis());
        return
                Jwts.builder().subject((userDetails.getUsername()))
                        .issuedAt(issuedDate)
                        .expiration(expiredDate)
                        .signWith(signKey())
                        .compact();
    }

    public String getNameFromJwt(String token) {
        return extractAllClaims(token).getSubject();
    }
      private SecretKey signKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(signKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
