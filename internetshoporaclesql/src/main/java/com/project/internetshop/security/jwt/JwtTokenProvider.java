package com.project.internetshop.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

  @Value("${jwt.secret}")
  private String secretKey;
  @Value("${jwt.authorization-header}")
  private String authorizationHeader;
  @Value("${jwt.expiration}")
  private Long expirationTimeInDay;

  private Key key;

  private final UserDetailsService userDetailsService;

  @Autowired
  public JwtTokenProvider(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @PostConstruct
  private void init(){
    key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(secretKey.getBytes(StandardCharsets.UTF_8)));
  }

  public String createToken(String username, String role) {
    Claims claims = Jwts.claims().setSubject(username);
    claims.put("role",role);

    return "Bearer " + Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(new Date())
        .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(expirationTimeInDay)))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean validateToken(String token){
    if (token == null || !token.startsWith("Bearer ")) {
      return false;
    }
    try{
      return Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token.replace("Bearer ", ""))
          .getBody().getExpiration().after(new Date());
    }catch (JwtException | IllegalArgumentException e){
      throw new JwtAuthorizationException("Jwt token is expired or invalid", HttpStatus.UNAUTHORIZED);
    }
  }

  public Authentication getAuthentication(String token){
    UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
    return new UsernamePasswordAuthenticationToken(userDetails,"", userDetails.getAuthorities());
  }

  public String resolveToken(HttpServletRequest request){
    return request.getHeader(authorizationHeader);
  }

  private String getUsername(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

}
