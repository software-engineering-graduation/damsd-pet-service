package br.petservice.backend.security;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.jsonwebtoken.Jwts.builder;
import static io.jsonwebtoken.Jwts.parserBuilder;
import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static io.jsonwebtoken.io.Decoders.BASE64;
import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static java.lang.System.currentTimeMillis;

/**
 * Serviço de manipulação de tokens JWT.
 * Esta classe é responsável por gerar, validar e extrair informações de tokens JWT.
 */
@Service
@Getter
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * Gera um token JWT para um usuário.
     *
     * @param userDetails Os detalhes do usuário para quem o token é gerado.
     * @return O token JWT gerado.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, jwtExpiration);
    }

    /**
     * Valida um token JWT em relação aos detalhes do usuário.
     *
     * @param token       O token JWT a ser validado.
     * @param userDetails Os detalhes do usuário a serem comparados.
     * @return Verdadeiro se o token for válido, falso caso contrário.
     */
    public boolean isTokenValid(String token, @NotNull UserDetails userDetails) {

        String username = extractUsername(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Extrai o nome de usuário de um token JWT.
     *
     * @param token O token JWT de onde o nome de usuário será extraído.
     * @return O nome de usuário extraído.
     */
    public String extractUsername(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * Gera um token JWT a partir de reivindicações e detalhes do usuário.
     *
     * @param claims      Reivindicações a serem incluídas no token.
     * @param userDetails Os detalhes do usuário.
     * @param expiration  O tempo de expiração do token.
     * @return O token JWT gerado.
     */
    private String generateToken(Map<String, Object> claims, @NotNull UserDetails userDetails, long expiration) {
        return builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(currentTimeMillis()))
                .setExpiration(new Date(currentTimeMillis() + expiration))
                .signWith(getSigningKey(), HS256)
                .compact();
    }

    /**
     * Obtém a chave de assinatura a partir da chave secreta.
     *
     * @return A chave de assinatura.
     */
    private @NotNull Key getSigningKey() {
        return hmacShaKeyFor(BASE64.decode(secret));
    }

    /**
     * Obtém as reivindicações a partir de um token JWT.
     *
     * @param token O token JWT de onde as reivindicações serão extraídas.
     * @return As reivindicações do token.
     */
    private Claims getClaimsFromToken(String token) {
        return parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Verifica se um token JWT está expirado.
     *
     * @param token O token JWT a ser verificado.
     * @return Verdadeiro se o token estiver expirado, falso caso contrário.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrai a data de expiração de um token JWT.
     *
     * @param token O token JWT de onde a data de expiração será extraída.
     * @return A data de expiração do token.
     */
    private Date extractExpiration(String token) {
        return getClaimsFromToken(token).getExpiration();
    }
}
