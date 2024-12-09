package br.petservice.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;

import static br.petservice.backend.util.Constants.AUTHORIZATION_HEADER;
import static br.petservice.backend.util.Constants.BEARER_TOKEN_PREFIX;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

/**
 * Filtro de autenticação JWT.
 * Esta classe estende {@link OncePerRequestFilter} e é responsável por interceptar
 * as requisições HTTP, extrair e validar o token JWT, e configurar o contexto de segurança
 * da aplicação com as informações do usuário autenticado.
 */
@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Executa o filtro de autenticação JWT.
     *
     * @param request     O objeto HttpServletRequest que contém a requisição.
     * @param response    O objeto HttpServletResponse que será usado para enviar a resposta.
     * @param filterChain O filtro da cadeia que deve ser chamado após este filtro.
     * @throws ServletException Se ocorrer um erro durante a execução do filtro.
     * @throws IOException      Se ocorrer um erro de I/O.
     */
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        log.info("doFilterInternal() - executando filtro de autenticação e autorização");

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (isNull(authHeader) || !authHeader.startsWith(BEARER_TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwtToken = authHeader.substring(BEARER_TOKEN_PREFIX.length());
            String username = jwtService.extractUsername(jwtToken);
            Authentication authentication = getContext().getAuthentication();

            if (nonNull(username) && isNull(authentication)) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
            throw new AuthenticationException("Falha ao autenticar usuário.", e);
        }
    }
}