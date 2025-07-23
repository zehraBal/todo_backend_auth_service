package com.bal.auth_service.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration.ms}")
    private long EXPIRATION_MS;

    // Get the username (subject) information from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Public method to retrieve a specific claim (property) from the token
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Generate a JWT token containing the username
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // "subject" → genellikle username olur
                .setIssuedAt(new Date(System.currentTimeMillis())) // ne zaman üretildi
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS)) // 10 saat geçerli
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // HS256 algoritması ile imzala
                .compact(); // Token'ı oluştur
    }

    // Check if the token is valid or not
    public boolean isTokenValid(String token,String username){
        final String extractedUsername=extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    // Check if the token is expired or not
    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    // Retrieve expiration date from token
    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);

    }

    // Public method to retrieve all claims from the token
    private Claims extractAllClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();   // getBody()
    }
    /*
    * example Claims object
    * {
    * "sub": "user123",
    * "iat": 1650264800,
    * "exp": 1650268400
    * }
    * */

}
