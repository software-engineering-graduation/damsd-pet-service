package br.petservice.backend.model;

import br.petservice.backend.model.abstracts.BaseEntity;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "category_tag")
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CategoryTag extends BaseEntity {

    @Column(nullable = false)
    @Schema(description = "Nome da categoria do serviço.", example = "Cuidado de Animais")
    private String categoryName;
}
