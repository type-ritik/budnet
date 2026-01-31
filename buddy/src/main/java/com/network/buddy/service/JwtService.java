package com.network.buddy.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.network.buddy.model.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    public UUID extractId(String token) {
        Object idClaim = extractAllClaims(token).get("id");
        if (idClaim == null) {
            log.error("ID claim is missing in the token");
            throw new RuntimeException("ID claim is missing in the token");
        } else {
            return UUID.fromString(idClaim.toString());
        }
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public Claims extractAllClaims(String token) {
        Claims claim = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
        return claim;
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims) // Set the custom map first
                .setSubject(username) // Standard claim 'sub'
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
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

    public String parseJwt(StompHeaderAccessor accessor) {
        String token = accessor.getFirstNativeHeader("Authorization");
        String jwt = null;
        if (token != null) {
            jwt = token.substring(7);
        }
        return jwt;
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

}
