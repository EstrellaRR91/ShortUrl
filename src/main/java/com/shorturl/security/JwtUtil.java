package com.shorturl.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

        private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

        
    @Value("${jwt.secret}")
        private  String JwtSecret = "micontraseñalargalarguisimaparaasegurarlotodo";

    @Value("${jwt.expiration.ms}")
        private  int jwtExpirationMs = 32000000;

        private Key key = Keys.hmacShaKeyFor(JwtSecret.getBytes(StandardCharsets.UTF_8));

    public JwtUtil(int jwtExpirationMs, String JwtSecret){
         this.JwtSecret = JwtSecret;
         this.jwtExpirationMs = jwtExpirationMs;
         this.key= Keys.hmacShaKeyFor(JwtSecret.getBytes(StandardCharsets.UTF_8));
        }

    public String generateJwtToken(String username, List<String> roles){

        Date now = new Date();
        Date expiryDate = new Date (now.getTime()+ jwtExpirationMs);

        return Jwts.builder()
        .setSubject(username)
        .claim("roles", roles)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(getSigninKey(),SignatureAlgorithm.HS256)
        .compact();

        }

        private Key getSigninKey() {
     
        return key;
    }

        public String getUserNameFromJwtToken(String token){
        return  Jwts.parserBuilder()
        .setSigningKey(getSigninKey())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
        }

        public boolean validateJwtToken( String token){
        try{
            Jwts.parserBuilder()
            .setSigningKey(getSigninKey())
            .build()
            .parseClaimsJws(token);
            return true;

        }catch(SecurityException | MalformedJwtException e){
            logger.error("Firma JWT no válida: {}", e.getMessage());
        }catch (ExpiredJwtException e){
            logger.error("Token JWT expirado: {}", e.getMessage());
        }catch (UnsupportedJwtException e){
            logger.error("Token JWT no soportado: {}", e.getMessage());
        } catch (IllegalArgumentException e){
            logger.error("Cadena de claims JWT vacía: {}", e.getMessage());
        }

        return false;
}

    @SuppressWarnings("unchecked")
    public List<String> getRolesFromJwtToken(String token) {
         Claims claims  =Jwts.parserBuilder()
        .setSigningKey(getSigninKey())
        .build()
        .parseClaimsJws(token)
        .getBody();

    return (List<String>) claims.get("roles");
}


}
