package com.inn.cafe.jwt;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final  static String SECRET_KEY="4226452948404D635166546A576E5A7234753778214125432A462D4A614E6452";

    private final Set<String> revokedTokens = new HashSet<>(); // Store revoked tokens




    public String extractUSername(String token){

        return extractClaim(token , Claims::getSubject);
    }

    public <T> T extractClaim(String token , Function<Claims,T> resolveClaim){
        final Claims claim=extractAllClaims(token);
         return   resolveClaim.apply(claim);
    }

    public Date extractExpiration(String token){

        return extractClaim(token , Claims::getExpiration);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignkey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignkey() {
        byte [] keyByte= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyByte);
    }

    private boolean isTokenExpired(String token){

        return extractExpiration(token).before(new Date());

      }
    private boolean isTokenExpiredOrRevoked(String token) {
        return isTokenExpired(token) || isTokenRevoked(token);
    }
    private boolean isTokenRevoked(String token) {
        return revokedTokens.contains(token);
    }
    public void revokeToken(String token) {
        revokedTokens.add(token);
    }
  public boolean isTokenValid(String token , UserDetails userDetails){

        final  String uSername =extractUSername(token);
        return (uSername.equals(userDetails.getUsername()) && !isTokenExpiredOrRevoked(token));
  }
public String generateTokenWithOnlyUserName(String username){

        return genertaeToken(new HashMap<>(),username);
}

public String genrateTokenWithRole(String username , String role){
        Map<String, Object> claim=new HashMap<>();
    claim.put("role",role);
        return genertaeToken(claim,username);
}

  private String genertaeToken(
          Map<String ,Object> extratClaims,
          String subject //username or email

  ){
      return Jwts
              .builder()
              .setClaims(extratClaims)
              .setSubject(subject)
              .setIssuedAt(new Date(System.currentTimeMillis()))
              .setExpiration(new Date(System.currentTimeMillis()+1000 * 60 * 60 * 10))  //token expired in 10 hours
              .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
              .compact();

  }


}
