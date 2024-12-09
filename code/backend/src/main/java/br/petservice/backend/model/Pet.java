package br.petservice.backend.model;

import br.petservice.backend.model.abstracts.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static br.petservice.backend.util.SetUtil.setIfNotNull;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Data
@Entity
@Table(name = "pets")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Entidade que representa um Pet no sistema")
public class Pet extends BaseEntity {

    @Valid
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "pet_guardian_id", referencedColumnName = "id")
    @JsonProperty(access = WRITE_ONLY)
    @Schema(description = "Guardião responsável pelo pet")
    private PetGuardian petGuardian;

    @Column(nullable = false)
    @Pattern(regexp = "^[\\p{L}\\p{N} .,'@&-]+$", message = "O campo só deve conter letras, números e símbolos permitidos")
    @Schema(description = "Nome do pet", example = "Buddy", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Column(nullable = false)
    @Pattern(regexp = "^[\\p{L}\\p{N} .,'@&-]+$", message = "O campo só deve conter letras, números e símbolos permitidos")
    @Schema(description = "Espécie do pet", example = "Cachorro", requiredMode = Schema.RequiredMode.REQUIRED)
    private String specie;

    @Column(nullable = false)
    @Pattern(regexp = "^[\\p{L}\\p{N} .,'@&-]+$", message = "O campo só deve conter letras, números e símbolos permitidos")
    @Schema(description = "Raça do pet", example = "Golden Retriever", requiredMode = Schema.RequiredMode.REQUIRED)
    private String race;

    @Column(nullable = false)
    @Schema(description = "Peso do pet em quilogramas", example = "15.5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double weight;

    @Column(nullable = false)
    @Schema(description = "Data de nascimento do pet", example = "2018-05-19", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate birthDate;

    @Column
    @Schema(description = "Url da imagem do pet", example = "https://i.ibb.co/jhdnKs7/pet.jpg")
    private String image;

    /**
     * Método para atualizar as informações do pet.
     *
     * @param pet Dados do pet com os valores a serem atualizados
     * @return O pet atualizado
     */
    public Pet update(@NotNull Pet pet) {
        setIfNotNull(this::setName, pet.getName());
        setIfNotNull(this::setSpecie, pet.getSpecie());
        setIfNotNull(this::setRace, pet.getRace());
        setIfNotNull(this::setWeight, pet.getWeight());
        setIfNotNull(this::setBirthDate, pet.getBirthDate());
        setIfNotNull(this::setUpdatedAt, LocalDateTime.now());
        setIfNotNull(this::setImage, pet.getImage());
        return this;
    }
}
