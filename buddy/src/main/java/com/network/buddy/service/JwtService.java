package com.network.buddy.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.network.buddy.model.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // Environment data import
    @Value("${application.security.jwt.secret-key}")
    private String SEC_KEY;
    @Value("${application.security.jwt.expiration}")
    private long expiration;

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SEC_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().build().parseClaimsJws(token).getBody();
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    public String generateToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());
        return createToken(claims, user.getUsername());
    }

    public Boolean isTokenValide(String token, UserDetails userDetails) {
        Date expireDate = extractExpiration(token);
        if (expireDate.before(new Date())) {
            return false;
        }

        String username = extractUsername(token);
        return userDetails.getUsername().equals(username) && !expireDate.before(new Date());
    }

}
