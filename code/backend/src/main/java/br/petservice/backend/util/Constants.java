package br.petservice.backend.util;

import lombok.experimental.UtilityClass;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@UtilityClass
public class Constants {

    /* Requisição e autenticação */

    public static String AUTHORIZATION_HEADER = "Authorization";

    public static String BEARER_TOKEN_PREFIX = "Bearer ";

    public static String CORS_CONFIGURATION = "/**";

    public static String[] PERMITTED_MATCHERS = {"*/auth/**", "swagger-ui/**", "swagger-ui**", "/v3/api-docs/**", "/v3/api-docs**", "/address/**", "/ws/**"};

    public static List<String> AUTHENTICATION_ERROR_HEADERS = asList("www-authenticate", "Bearer realm='/auth/authenticate'");

    public static List<String> ALLOWED_METHODS = asList("GET", "POST", "PUT", "DELETE", "OPTIONS");

    public static List<String> ALLOWED_HEADERS = asList("Authorization", "Cache-Control", "Content-Type");

    public static List<String> ALLOWED_ORIGINS = singletonList("*");

    public static String VIA_CEP_URL = "https://viacep.com.br/ws/";

    public static String VIA_CEP_RETURN_TYPE = "/json/";

    /* Manipulação de dados */

    public static int INDEX_ZERO = 0;

    public static int INDEX_ONE = 1;

    /* Swagger */

    public static String SECURITY_ITEMS = "bearerAuth";

    public static String SECURITY_SCHEME_NAME = "Bearer token";

    public static String SECURITY_SCHEME_BEARER_SCHEME = "bearer";

    public static String SECURITY_SCHEME_BEARER_FORMAT = "JWT";

}
