package br.petservice.backend.model.dto;

import br.petservice.backend.model.Pet;
import br.petservice.backend.model.PetEstablishment;
import br.petservice.backend.model.PetGuardian;
import br.petservice.backend.model.ServiceProvided;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AppointmentDto(Long id,PetGuardian petGuardian, PetEstablishment petEstablishment, Pet pet,
                             List<ServiceProvided> appointmentServices, Double totalValue, LocalDateTime dateTime) {
}
