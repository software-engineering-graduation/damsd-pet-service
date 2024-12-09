package br.petservice.backend.model.abstracts;

import br.petservice.backend.model.WorkingHours;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.br.CNPJ;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Entidade que representa uma Pessoa Jurídica, herda de User")
public abstract class LegalEntity extends User {

    @Embedded
    @Schema(description = "Horário de funcionamento do estabelecimento")
    private WorkingHours workingHours;

    @CNPJ
    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^\\d{14}$", message = "formato esperado: 14 dígitos numéricos consecutivos")
    @Schema(description = "CNPJ da entidade com 14 dígitos numéricos consecutivos.", example = "12345678000199")
    private String cnpj;

    @Column(nullable = false)
    @Pattern(regexp = "^[\\p{L}0-9 ]+$", message = "o campo só deve conter letras e pode conter acentos")
    @Schema(description = "Nome comercial da empresa", example = "PetShop XYZ")
    private String businessName;

}
