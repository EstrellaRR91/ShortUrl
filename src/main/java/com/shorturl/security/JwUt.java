package com.shorturl.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwUt {
        private final String jwSecret = "Chacho13Noa07!?";

        private final int jwExpirationMs = 32000000;

    public String generateJwtToken(String username){
        return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis()+ jwExpirationMs))
        .signWith(SignatureAlgorithm.HS512, jwSecret)
        .compact();

        }

        public String getUserNameFromJwtToken(String token){
        return Jwts.parser()
        .setSigningKey(jwSecret)
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
        }

        public boolean validateJwtToken( String authToken){
        try{
            Jwts.parser().setSigningKey(jwSecret).parseClaimsJws(authToken);
            return true;
        }catch(SignatureException e){
            System.err.println("Firma JWT no válida:"+ e.getMessage());
        }catch (MalformedJwtException e){
            System.err.println("Token JWT mal formado:"+ e.getMessage());
        }catch (ExpiredJwtException e){
            System.err.println("Token JWT expirado:"+ e.getMessage());
        }catch (UnsupportedJwtException e){
        System.err.println("Token JWT no soportado:"+ e.getMessage());
        } catch (IllegalArgumentException e){
        System.err.println("Cadena de claims JWT vacía:"+ e.getMessage());
        }

        return false;
}

}
