package br.petservice.backend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessagesEnum {

    MSG_USER_NOT_FOUND_EXCEPTION("Usuário não encontrado."),
    MSG_PET_NOT_FOUND_EXCEPTION("Pet não encontrado."),
    MSG_CATEGORY_TAG_NOT_FOUND_EXCEPTION("Tag de categoria não encontrada: %s."),
    MSG_SERVICE_PROVIDED_NOT_FOUND_EXCEPTION("Serviço não encontrado."),
    MSG_CATEGORY_TAG_FOUND_EXCEPTION("Categoria de serviço não encontrada."),
    MSG_APPOINTMENT_FOUND_EXCEPTION("Agendamento não encontrado."),
    MSG_REVIEW_FOUND_EXCEPTION("Avaliação não encontrada."),
    MSG_PET_ESTABLISHMENT_NOT_FOUND_EXCEPTION("Estabelecimento não encontrado.");

    private final String message;
}
