package br.petservice.backend.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.io.IOException;

import static io.jsonwebtoken.Header.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.*;

/**
 * ExceptionInterceptor é um interceptador global de exceções para a aplicação.
 * Ele captura e trata exceções comuns, retornando respostas apropriadas aos clientes.
 * Também lida com falhas de autenticação e a formatação de respostas com base em erros de validação,
 * conflitos de integridade de dados, e outras falhas esperadas.
 */
@Component
@RestControllerAdvice
public class ExceptionInterceptor extends DefaultHandlerExceptionResolver implements AuthenticationFailureHandler {

    @Value("${server.error.include-exception}")
    private boolean imprimirStackTrace;

    /**
     * Método que lida com falhas de autenticação.
     * Retorna uma mensagem amigável quando a autenticação falha.
     *
     * @param request   A requisição HTTP.
     * @param response  A resposta HTTP.
     * @param exception A exceção de autenticação.
     * @throws IOException Lança uma exceção caso haja problema ao escrever a resposta.
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, @NotNull HttpServletResponse response, AuthenticationException exception) throws IOException {

        int statusHttp = HttpStatus.UNAUTHORIZED.value();
        ErrorResponse errorResponse = new ErrorResponse(statusHttp, "Usuário ou senha incorreto.");

        response.setStatus(statusHttp);
        response.setContentType(CONTENT_TYPE);
        response.getWriter().append(errorResponse.toJson());
    }

    /**
     * Captura e trata exceções de validação de argumentos do método.
     * Retorna um erro de status 422 (Unprocessable Entity) com detalhes sobre os campos inválidos.
     *
     * @param e       A exceção capturada.
     * @return Uma resposta HTTP com o status e os detalhes da violação.
     */
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> captureUnprocessableEntity(@NotNull MethodArgumentNotValidException e) {

        ErrorResponse errorResponse = new ErrorResponse(UNPROCESSABLE_ENTITY.value(), "Erro de validação.");

        for (FieldError fieldError : e.getBindingResult().getFieldErrors())
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());

        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    /**
     * Captura e trata exceções de violação de integridade de dados, como tentativas de salvar dados duplicados.
     * Retorna um erro de status 409 (Conflict).
     *
     * @param e       A exceção capturada.
     * @param request O objeto de requisição.
     * @return Uma resposta HTTP com o status e a mensagem de erro.
     */
    @ResponseStatus(CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> captureConflict(@NotNull DataIntegrityViolationException e, WebRequest request) {

        String msgErro = e.getMostSpecificCause().getMessage();

        return buildErrorMessage(e, msgErro, CONFLICT, request);
    }

    /**
     * Captura e trata exceções quando uma entidade não é encontrada no banco de dados.
     * Retorna um erro de status 404 (Not Found).
     *
     * @param e       A exceção capturada.
     * @param request O objeto de requisição.
     * @return Uma resposta HTTP com o status e a mensagem de erro.
     */
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> captureNotFound(@NotNull EntityNotFoundException e, WebRequest request) {

        String msgErro = e.getMessage();

        return buildErrorMessage(e, msgErro, NOT_FOUND, request);
    }

    /**
     * Captura e trata exceções relacionadas a argumentos de método inválidos ou leituras incorretas de mensagens HTTP.
     * Retorna um erro de status 400 (Bad Request).
     *
     * @param e       A exceção capturada.
     * @param request O objeto de requisição.
     * @return Uma resposta HTTP com o status e a mensagem de erro.
     */
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class, ConstraintViolationException.class})
    public ResponseEntity<Object> captureBadRequest(@NotNull Exception e, WebRequest request) {

        String msgErro = e.getMessage();

        return buildErrorMessage(e, msgErro, HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Constrói uma mensagem de erro padronizada a partir de uma exceção, status HTTP e a requisição.
     * Inclui o stack trace da exceção se a propriedade `imprimirStackTrace` estiver habilitada.
     *
     * @param e          A exceção capturada.
     * @param message    A mensagem de erro personalizada.
     * @param httpStatus O status HTTP correspondente.
     * @param request    O objeto de requisição.
     * @return Uma resposta HTTP com a mensagem de erro formatada.
     */
    private @NotNull ResponseEntity<Object> buildErrorMessage(Exception e, String message, @NotNull HttpStatus httpStatus, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);

        if (imprimirStackTrace)
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(e));

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
