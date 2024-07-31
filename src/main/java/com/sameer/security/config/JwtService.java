package com.sameer.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY="eQbsTo7+VRd+q40C+N7B+rB59Pmg7Im+rEp71snA07kQ2eH/nYy9t+XEVhpyNeRz";
    public String extractUserName(String token){
        return extractClaim(token,Claims::getSubject);
    }
    private boolean isTokenExpired(String token){
        if(token==null || token.isEmpty()){
            return false;
        }
        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token)
    {
        return extractClaim(token,Claims::getExpiration);
    }
    public boolean isTokenValid(String token ,UserDetails userDetails){
      final String username = extractUserName(token);
      if(username.equals(userDetails.getUsername()) && !isTokenExpired(token))
          return true;
      return false;



    }
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims= extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private Key getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String generateToken(UserDetails userDetails){
return generateToken(new HashMap<>(),userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+100*60*60*24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
