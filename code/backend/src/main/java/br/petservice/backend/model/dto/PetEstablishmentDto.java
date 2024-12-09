package br.petservice.backend.model.dto;

import br.petservice.backend.model.Address;
import br.petservice.backend.model.WorkingHours;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import org.springframework.hateoas.Link;

import java.util.List;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PetEstablishmentDto(
        Long id,
        String email,
        String role,
        List<Link> links,
        Address address,
        String businessName,
        String phoneNumber,
        WorkingHours workingHours,
        String cnpj,
        String image,
        Double averageRating) {
}
