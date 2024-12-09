package br.petservice.backend.configuration;

import br.petservice.backend.security.JwtAuthenticationEntryPoint;
import br.petservice.backend.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static br.petservice.backend.util.Constants.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Configuração de segurança da aplicação.
 * Esta classe configura a segurança da aplicação, incluindo a autenticação JWT,
 * gerenciamento de sessões, CORS e regras de autorização para as requisições HTTP.
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Configura o filtro de segurança da aplicação.
     *
     * @param http O objeto HttpSecurity para configurar a segurança.
     * @return O SecurityFilterChain configurado.
     * @throws Exception Se ocorrer um erro durante a configuração.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        log.info("securityFilterChain() - iniciando a configuração do filtro de segurança.");

        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(PERMITTED_MATCHERS).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> {

                    log.info("securityFilterChain() - configurando gerenciamento de sessões.");

                    session.sessionCreationPolicy(STATELESS);
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider)
                .exceptionHandling(e -> {

                    log.info("securityFilterChain() - configurando tratamento de exceções.");

                    e.authenticationEntryPoint(new JwtAuthenticationEntryPoint());
                })
                .build();
    }

    /**
     * Configura as regras de CORS para a aplicação.
     *
     * @return Um CorsConfigurationSource configurado.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        log.info(">>> corsConfigurationSource: iniciando configuração de Cors");
        final CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        //configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        //configuration.setAllowedOrigins(Arrays.asList("*"));

        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Cache-Control",
                "Content-Type",
                "Sec-WebSocket-Key",
                "Sec-WebSocket-Version",
                "Sec-WebSocket-Extensions"
        ));
        configuration.setAllowCredentials(true); // Permitir credenciais
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica a configuração para todas as rotas

        return source;
    }
}

