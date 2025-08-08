package com.glowapex.config;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import com.glowapex.exception.JwtAuthenticationException;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getEmailFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            throw new JwtAuthenticationException("JWT token is expired", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("Invalid JWT token", HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new JwtAuthenticationException("JWT token is expired", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("Invalid JWT token", HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}