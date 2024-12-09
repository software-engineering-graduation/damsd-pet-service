package br.petservice.backend.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ReviewRequestDto(
        Long author_id,
        Long reviewed_user_id,
        int rating,
        String comment) {
}
