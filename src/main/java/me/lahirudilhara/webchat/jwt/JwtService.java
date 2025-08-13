package me.lahirudilhara.webchat.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {
    private final String jwtToken = "3X+7oKlgh4r5uIzrVjOx3HVlMvC9Dr8E38Wn3ppdURPlN6nSkpFZ0KShZkyBRBo5s5t7Fu7VxBmcH0mjCh7qZw==";
    private final int expirationInHours = 21600000;

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date((System.currentTimeMillis())))
                .expiration(new Date(System.currentTimeMillis() + this.expirationInHours))
                .and()
                .signWith(getSecretKey())
                .compact();
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = jwtToken.getBytes();;
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token){
        return this.extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails){
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpiered(token));
    }

    private boolean isTokenExpiered(String token){
        return extractExpireation(token).before(new Date());
    }

    private Date extractExpireation(String token){
        return extractClaim(token,Claims::getExpiration);
    }
}
