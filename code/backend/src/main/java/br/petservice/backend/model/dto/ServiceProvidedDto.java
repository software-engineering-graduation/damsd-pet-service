package br.petservice.backend.model.dto;

import br.petservice.backend.model.PetEstablishment;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import org.springframework.hateoas.Link;

import java.util.List;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ServiceProvidedDto(
        Long id,
        PetEstablishment petEstablishment,
        String name,
        Double value,
        String categoryName,
        String description,
        Double averageRating,
        List<Link> links) {
}
