package user_service.configuration.JwtConfig;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
//import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import user_service.entities.UserInfo;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtLogic {
    private static final String SECRET_KEY = "Z3jFd92hfkdjgs&8fjkd#GhT@5r3!7nqLtRZsE";
        private static final long EXPIRATION_TIME = 900000; // 1 day in milliseconds

    private Key key;



    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

/*
    public  String generateToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());


        claims.put("email", email);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, getSignInKey())
                .compact();
    }*/
//    public String getUserNameFromJwtToken(String token) {
//        return Jwts
//                .parserBuilder()
//                .setSigningKey(SECRET_KEY.getBytes())
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }

    public String generateToken(String username, String role) {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
    try {
     //   Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        // Parsează token-ul JWT
        Claims claims = Jwts.parserBuilder()
              .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token) // Validează și parsează
                .getBody();

        // Returnează subiectul (email sau ID utilizator)
        return claims.getSubject();

    } catch (SignatureException e) {
        throw new RuntimeException("Semnătura token-ului JWT este invalidă");
    } catch (MalformedJwtException e) {
        throw new RuntimeException("Token JWT invalid");
    } catch (ExpiredJwtException e) {
        throw new RuntimeException("Token JWT expirat");
    } catch (UnsupportedJwtException e) {
        throw new RuntimeException("Token JWT nesuportat");
    } catch (IllegalArgumentException e) {
        throw new RuntimeException("Câmpuri lipsă în token-ul JWT");
    }
}




    private static Key getSignInKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}
