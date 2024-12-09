package br.petservice.backend.model;

import br.petservice.backend.model.abstracts.BaseEntity;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

import static br.petservice.backend.util.SetUtil.setIfNotNull;

@Data
@Entity
@Table(name = "reviews")
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "Representa uma avaliação feita por um usuário sobre outro.")
public class Review extends BaseEntity {

    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @Schema(description = "Usuário que fez a avaliação.", accessMode = Schema.AccessMode.WRITE_ONLY)
    private Long authorId;

    @JoinColumn(name = "reviewed_user_id", referencedColumnName = "id")
    @Schema(description = "Usuário que recebeu a avaliação.", accessMode = Schema.AccessMode.WRITE_ONLY)
    private Long reviewedUserId;

    @JoinColumn(name = "reviewed_pet_id", referencedColumnName = "id")
    @Schema(description = "Pet que recebeu a avaliação.", accessMode = Schema.AccessMode.WRITE_ONLY)
    private Long reviewedPetId;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    @Schema(description = "Nota da avaliação. Deve estar entre 1 e 5.", example = "4")
    private int rating;

    @Schema(description = "Comentário da avaliação.")
    private String comment;

    public Review update(@NotNull Review review) {
        setIfNotNull(this::setAuthorId, review.getAuthorId());
        setIfNotNull(this::setReviewedUserId, review.getReviewedUserId());
        setIfNotNull(this::setRating, review.getRating());
        setIfNotNull(this::setComment, review.getComment());
        setIfNotNull(this::setUpdatedAt, LocalDateTime.now());
        return this;
    }
}
