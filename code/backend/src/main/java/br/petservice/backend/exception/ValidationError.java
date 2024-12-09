package br.petservice.backend.exception;

public record ValidationError(String field, String message) {
}
