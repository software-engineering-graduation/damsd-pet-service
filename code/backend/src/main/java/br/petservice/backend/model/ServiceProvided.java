package br.petservice.backend.model;

import br.petservice.backend.model.abstracts.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import static br.petservice.backend.util.SetUtil.setIfNotNull;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServiceProvided extends BaseEntity {

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "establishment_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private PetEstablishment petEstablishment;

    @ManyToOne
    @JoinColumn(name = "category_tag_id", referencedColumnName = "id")
    @Schema(description = "Nome da categoria do serviço.", example = "Cuidado de Animais")
    private CategoryTag categoryTag;

    @Column
    @Schema(description = "Nome do serviço fornecido.", example = "Tosa de cães.")
    private String name;

    @Column
    @PositiveOrZero
    private Double value;

    @Column
    @Schema(description = "Descrição do serviço fornecido.", example = "Serviço de banho e tosa para cães.")
    private String description;

    @Transient
    @Schema(description = "Avaliação média do usuário, não persistida no banco de dados")
    private Double averageRating;

    /**
     * Atualiza os detalhes do serviço fornecido com as informações de outro serviço.
     *
     * @param serviceProvided objeto {@link ServiceProvided} contendo as informações a serem atualizadas
     * @return referência ao serviço atualizado
     */
    public ServiceProvided update(@NotNull ServiceProvided serviceProvided) {
        setIfNotNull(this::setPetEstablishment, serviceProvided.getPetEstablishment());
        setIfNotNull(this::setCategoryTag, serviceProvided.getCategoryTag());
        setIfNotNull(this::setDescription, serviceProvided.getDescription());
        setIfNotNull(this::setName, serviceProvided.getName());
        setIfNotNull(this::setValue, serviceProvided.getValue());
        return this;
    }
}
