package br.petservice.backend.model.dto;

import br.petservice.backend.model.Address;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import org.springframework.hateoas.Link;

import java.time.LocalDate;
import java.util.List;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PetGuardianDto(
        Long id,
        String email,
        String role,
        List<Link> links,
        Address address,
        String fullName,
        String phoneNumber,
        LocalDate birthDate,
        String image) {
}
