package com.salesianostriana.gameetapi.security.jwt;


import com.salesianostriana.gameetapi.security.models.user.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Log
@Service
public class JwtProvider {

    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    @Value("${jwt.secret:V79GUHScIFb2AE7jZogWMOwYnJIrvTIWyDhlA91iHhDKcZeLzeU5ULDGUdRFLH3EJ9v7mvW8DMChl_eg9Wh5-IfEvHM7Os6lXfrNcX4Ln4Seiym_djlZzi1ZDCvalxyCKIJZUsHJvlF6zC-YcMsjrbvWi9kYfkllmFdn9DpyezY}")
    private String jwtSecret;

    @Value("${jwt.duration:86400}") // 1 día
    private int jwtLifeInSeconds;

    private JwtParser parser;

    @PostConstruct
    public void init() {
        parser = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build();
    }

    public boolean validateToken(String token) {

        try {
            parser.parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException
                | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            log.info("Error in token! : " + ex.getMessage());
        }
        return false;

    }

    public UUID getUserIdFromJwt(String token) {
        return UUID.fromString(parser.parseClaimsJws(token).getBody().getSubject());
    }

    public String generateToken(Authentication authentication) {

        UserEntity user = (UserEntity) authentication.getPrincipal();

        Date tokenExpirationDate = Date
                .from(LocalDateTime
                        .now()
                        .plusSeconds(jwtLifeInSeconds)
                        .atZone(ZoneId.systemDefault()).toInstant());


        return Jwts.builder()
                .setHeaderParam("typ", TOKEN_TYPE)
                .setSubject(user.getId().toString())
                .setIssuedAt(tokenExpirationDate)
                .claim("fullName", user.getFullName())
                .claim("role", user.getRole().name())
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();


    }

}
