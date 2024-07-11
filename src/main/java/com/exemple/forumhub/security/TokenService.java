package com.exemple.forumhub.security;

import com.exemple.forumhub.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static org.springframework.security.config.Elements.JWT;

@Service
public class TokenService {

    private final String secret;
    private static final String ISSUER = "Forum API";

    public TokenService(@Value("${api.security.token.secret}") String secret) {
        this.secret = secret;
    }

    public String gerarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getEmail())
                    .withExpiresAt(Date.from(dataExpiracao()))
                    .sign(algorithm);
        } catch (JWTCreationException ex) {
            throw new RuntimeException("Erro ao gerar o token", ex);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException ex) {
            throw new RuntimeException("Token inválido ou expirado!", ex);
        }
    }

    private Date dataExpiracao() {
        return Date.from(LocalDateTime.now().plusHours(2)
                .toInstant(ZoneOffset.of("-03:00")));
    }
}
