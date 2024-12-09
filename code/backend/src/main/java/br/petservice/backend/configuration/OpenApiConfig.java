package br.petservice.backend.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static br.petservice.backend.util.Constants.*;
import static io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER;
import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;

/**
 * Configuração do OpenAPI para a API do serviço de pets.
 * Esta classe define as informações básicas da API e o esquema de autenticação.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Cria e configura uma instância do OpenAPI.
     *
     * @return uma instância configurada do OpenAPI contendo informações sobre a API
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Pet Service API")
                        .version("1.0.0")
                        .description("API para gerenciamento de serviços relacionados a pets, desenvolvida para o Trabalho Interdisciplinar V."))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_ITEMS))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(SECURITY_ITEMS,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(HTTP)
                                        .scheme(SECURITY_SCHEME_BEARER_SCHEME)
                                        .bearerFormat(SECURITY_SCHEME_BEARER_FORMAT)
                                        .in(HEADER)
                                        .description("Token de autenticação Bearer utilizado para acessar as operações da API.")));
    }
}
