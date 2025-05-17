package com.example.sfmproject.JWT;
import com.example.sfmproject.Entities.UserPrinciple;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${cryptoserver.app.jwtSecret}")
    private String jwtSecret;

    @Value("${cryptoserver.app.jwtExpiration}")
    private int jwtExpiration;

    public String generateAccessToken(Authentication authentication , String githubAccessToken) {
        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();

        long ACCESS_TOKEN_VALIDITY_MS = 5 * 24 * 60 * 60 * 1000; // 5 days
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("email", userPrincipal.getEmail()) // Add email claim
                .claim("roles", userPrincipal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority) // Extract roles
                        .collect(Collectors.toList()))
                .claim("githubAccessToken", githubAccessToken)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + ACCESS_TOKEN_VALIDITY_MS))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        //long refreshTokenExpiration =  10*60 * 1000;
        long refreshTokenExpiration = 2L * 7 * 24 * 60 * 60 * 1000;

        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();

        // Create refresh token
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))

                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }


    // Modify JwtProvider to generate and store refresh tokens in session
    public List<String> generateJwtTokens(Authentication authentication , String githubAccessToken) {
        // Generate access token as before
        String accessToken = generateAccessToken(authentication, githubAccessToken);

        // Generate refresh token
        String refreshToken = generateRefreshToken(authentication);

        // Create a list to hold both tokens
        List<String> tokens = new ArrayList<>();
        tokens.add(accessToken);
        tokens.add(refreshToken);

        // Return the list containing both tokens
        return tokens;
    }



    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature -> Message: {} ", e);
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            //     logger.error(" expired Access Token ", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Message: {}", e);
        }

        return false;
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
}
