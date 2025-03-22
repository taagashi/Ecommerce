package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.userDetails.MyUserDetails;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

import java.security.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public JWTService() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(MyUserDetails myUserDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", myUserDetails.getId());
        claims.put("role", myUserDetails.getAuthorities().iterator().next().getAuthority());
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(myUserDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 50))
                .and()
                .signWith(privateKey)
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    public boolean validateToken(String token) {
        return new Date(System.currentTimeMillis()).before(extractDate(token));
    }

    private Date extractDate(String token) {
            return extractClaims(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }
}
