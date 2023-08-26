package com.devops.tfm.login.security.jwt;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.devops.tfm.login.security.services.UserDetailsImpl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${bezkoder.app.jwtSecret}")
  private String jwtSecret;

  @Value("${bezkoder.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  @Value("${bezkoder.app.jwtCookieName}")
  private String jwtCookie;

  public String getJwtFromCookies(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, jwtCookie);
    if (cookie != null) {
      return cookie.getValue();
    } else {
      return null;
    }
  }

  public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
    String jwt = generateTokenFromUsername(userPrincipal.getUsername());
    return ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true)
        .build();
  }

  public ResponseCookie getCleanJwtCookie() {
    if (jwtCookie == null || jwtCookie.isEmpty()) {
      throw new IllegalArgumentException("JWT cookie cannot be null or empty.");
    }

    return ResponseCookie.from("jwt", jwtCookie)
        .path("/api")
        .build();
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key()).build()
        .parseClaimsJws(token).getBody().getSubject();
  }

  public Key key() {
    /*
     * String algorithm = SignatureAlgorithm.HS256.getJcaName();
     * byte[] secret = Decoders.BASE64.decode(jwtSecret);
     * return new SecretKeySpec(secret, algorithm);
     */
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  public boolean validateJwtToken(String authToken) {
    try {

      Jwts.parserBuilder()
          .setSigningKey(key())
          .build()
          .parseClaimsJws(authToken);

      return true;
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }

  public String generateTokenFromUsername(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setId(UUID.randomUUID().toString())
        .setIssuedAt(Date.from(Instant.now()))
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(key(), SignatureAlgorithm.HS256)
        .compact();
  }
}
