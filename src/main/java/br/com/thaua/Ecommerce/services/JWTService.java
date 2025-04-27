package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.userDetails.MyUserDetails;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

import java.security.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
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

        log.info("SERVICE JWT - GENERATE TOKEN");
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(myUserDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 50 * 100))
                .and()
                .signWith(privateKey)
                .compact();
    }

    public String extractEmail(String token) {
        log.info("SERVICE JWT - EXTRACT EMAIL");
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        log.info("SERVICE JWT - EXTRACT CLAIMS");
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    public boolean validateToken(String token) {
        log.info("SERVICE JWT - VALIDATE TOKEN");
        return new Date(System.currentTimeMillis()).before(extractDate(token));
    }

    private Date extractDate(String token) {
        log.info("SERVICE JWT - EXTRACT DATE");
        return extractClaims(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        log.info("SERVICE JWT - EXTRACT ALL CLAIMS");
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }
}
