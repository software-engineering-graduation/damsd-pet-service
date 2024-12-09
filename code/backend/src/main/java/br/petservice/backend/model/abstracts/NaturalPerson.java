package br.petservice.backend.model.abstracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Entidade que representa uma Pessoa Física, herda de User")
public abstract class NaturalPerson extends User {

    @CPF
    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^\\d{11}$", message = "formato esperado: 11 dígitos numéricos consecutivos")
    @Schema(description = "CPF da pessoa física com 11 dígitos numéricos consecutivos.", example = "12345678900")
    private String cpf;

    @Column(nullable = false)
    @Pattern(regexp = "^[\\p{L}0-9 ]+$", message = "o campo só deve conter letras e pode conter acentos")
    @Schema(description = "Nome completo da pessoa física", example = "João da Silva")
    private String fullName;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Data de nascimento no formato yyyy-MM-dd", example = "1985-08-15")
    private LocalDate birthDate;
}
