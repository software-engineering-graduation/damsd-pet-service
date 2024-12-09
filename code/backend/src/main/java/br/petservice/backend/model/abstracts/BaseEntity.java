package br.petservice.backend.model.abstracts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.SEQUENCE;
import static jakarta.persistence.InheritanceType.TABLE_PER_CLASS;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = TABLE_PER_CLASS)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "Classe base para entidades que contém propriedades comuns, como ID, createdAt e updatedAt")
public abstract class BaseEntity extends RepresentationModel<BaseEntity> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "seq_base_entity", sequenceName = "seq_base_entity", allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "seq_base_entity")
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    @Schema(description = "Identificador único da entidade", example = "1")
    private Long id;

    @JsonIgnore
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @Schema(description = "Data e hora da criação da entidade", hidden = true)
    private LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    @Column(nullable = false)
    @Schema(description = "Data e hora da última atualização da entidade", hidden = true)
    private LocalDateTime updatedAt;
}
