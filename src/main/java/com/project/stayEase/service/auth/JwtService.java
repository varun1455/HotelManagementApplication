package com.project.stayEase.service.auth;

import com.project.stayEase.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {


        @Value("${jwt.secretKey}")
        private String jwtSecretKey;

        @Value("${jwt.accessTokenExpiration}")
        private long accessTokenExpiration;

        @Value("${jwt.refreshTokenExpiration}")
        private long refreshTokenExpiration;

        private SecretKey getSigningKey(){
            byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
            return Keys.hmacShaKeyFor(keyBytes);
        }

        public String generateAccessToken(User user){

            return Jwts.builder()
                    .subject(user.getEmail())
                    .claim("userId", user.getId())
                    .claim("roles", user.getRoles())
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis()+ accessTokenExpiration))
                    .signWith(getSigningKey())
                    .compact();
        }

        public String generateRefreshToken(User user){
            return Jwts.builder()
                    .subject(user.getId().toString())
                    .claim("tokenType", "Refresh")
                    .issuedAt(new Date())
                    .expiration(
                            new Date(
                                    System.currentTimeMillis()
                                            + refreshTokenExpiration
                            )
                    )
                    .signWith(getSigningKey())
                    .compact();
        }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getUserIdFromToken(String token){
            Claims claims = extractAllClaims(token);
            return claims.get("userId", Long.class);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token)
                .getSubject();
    }

    public String getRefreshTokenFromCookies(HttpServletRequest request){
            if(request.getCookies()==null) return null;
            for(Cookie cookie : request.getCookies()){
                if("refreshToken".equals(cookie.getName())) return cookie.getValue();
            }

            return null;
    }

    public boolean isRefreshTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);

            String tokenType = claims.get("type", String.class);

            return "REFRESH".equals(tokenType)
                    && claims.getExpiration().after(new Date());

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}
