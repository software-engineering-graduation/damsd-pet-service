package br.petservice.backend.model.dto;

import java.util.Map;

public record MessageDto(String comboId, Map<Integer, String> messages) {
}
