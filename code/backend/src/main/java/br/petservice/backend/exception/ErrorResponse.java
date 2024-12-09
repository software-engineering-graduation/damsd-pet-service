package br.petservice.backend.exception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Data
public class ErrorResponse {

    private final int status;
    private final String message;
    private String stackTrace = "";
    private List<ValidationError> errorList;

    /**
     * Adiciona um novo erro de validação à RespostaErro
     *
     * @param field    field do erro
     * @param message message de erro
     */
    public void addValidationError(String field, String message) {

        if (isNull(errorList))
            this.errorList = new ArrayList<>();

        this.errorList.add(new ValidationError(field, message));
    }

    /**
     * Converte RespostaErro para JSON
     *
     * @return RespostaErro em formato JSON
     */
    public String toJson() {
        return "{\"status\": " + getStatus() + ", " +
                "\"message\": \"" + getMessage() + "\"}";
    }
}
