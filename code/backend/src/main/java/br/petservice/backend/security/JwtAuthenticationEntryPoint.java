package br.petservice.backend.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

import static br.petservice.backend.util.Constants.*;

/**
 * Ponto de entrada para autenticação JWT.
 * Esta classe implementa a interface {@link AuthenticationEntryPoint} e é responsável
 * por lidar com falhas de autenticação, enviando a resposta apropriada quando
 * a autenticação não é bem-sucedida.
 */
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Este método é chamado quando a autenticação falha.
     *
     * @param request       O objeto HttpServletRequest que contém informações sobre a requisição.
     * @param response      O objeto HttpServletResponse que será usado para enviar a resposta.
     * @param authException A exceção de autenticação lançada.
     * @throws IOException Se ocorrer um erro ao enviar a resposta.
     */
    @Override
    public void commence(HttpServletRequest request,
                         @NotNull HttpServletResponse response,
                         @NotNull AuthenticationException authException) throws IOException {

        response.setHeader(AUTHENTICATION_ERROR_HEADERS.get(INDEX_ZERO), AUTHENTICATION_ERROR_HEADERS.get(INDEX_ONE));
        response.sendError(HttpStatus.UNAUTHORIZED.value(), authException.getMessage());
    }
}
