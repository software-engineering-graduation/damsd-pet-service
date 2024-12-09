package br.petservice.backend.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PetDto(
        Long id,
        String name,
        String specie,
        String race,
        Double weight,
        LocalDate birthDate,
        String image) {
}
