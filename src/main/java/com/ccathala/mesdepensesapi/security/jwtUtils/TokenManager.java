package com.ccathala.mesdepensesapi.security.jwtUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenManager implements Serializable {

    /**
     * 
     */
    public static final long TOKEN_VALIDITY = 10 * 60 * 60;

    /**
     * 
     */
    @Value("${secret}")
    private String jwtSecret;

    /**
     * 
     * @param userDetails
     * @return
     */
    public String generateJwtToken(UserDetails userDetails) { 
        Map<String, Object> claims = new HashMap<>(); 
        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername()) 
           .setIssuedAt(new Date(System.currentTimeMillis())) 
           .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000)) 
           .signWith(SignatureAlgorithm.HS512, jwtSecret).compact(); 
     } 

    /**
     * 
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateJwtToken(String token, UserDetails userDetails) { 
    String username = getUsernameFromToken(token); 
    Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    Boolean isTokenExpired = claims.getExpiration().before(new Date()); 
    return (username.equals(userDetails.getUsername()) && !isTokenExpired); 
    }

    /**
     * 
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
        final Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody(); 
        return claims.getSubject(); 
     }

}
