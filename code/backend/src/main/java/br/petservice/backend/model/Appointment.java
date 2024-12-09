package br.petservice.backend.model;

import br.petservice.backend.model.abstracts.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.Fetch;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "appointments")
public class Appointment extends BaseEntity {

    @Column
    @Schema(description = "Tutor que agendou serviço.", accessMode = Schema.AccessMode.WRITE_ONLY)
    private Long petGuardianId;

    @Column
    @Schema(description = "Pet vinculado ao serviço.", accessMode = Schema.AccessMode.WRITE_ONLY)
    private Long petId;

    @Column
    @Schema(description = "Estabelecimento que realizará o serviço.", accessMode = Schema.AccessMode.WRITE_ONLY)
    private Long petEstablishmentId;

    @Valid
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinTable(name = "appointments_service_provided",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "service_provided_id"))
    private List<ServiceProvided> servicesProvided;

    @Column
    @PositiveOrZero
    private Double totalValue;

    @Column
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dateTime;
}
