package br.petservice.backend.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static br.petservice.backend.util.SetUtil.setIfNotNull;
import static jakarta.persistence.FetchType.EAGER;

@Data
@Embeddable
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WorkingHours {

    @ElementCollection(fetch = EAGER)
    @Schema(description = "Conjunto de dias da semana em que o estabelecimento está aberto.")
    private Set<DayOfWeek> daysOfWeek = new HashSet<>();

    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @Schema(description = "Horário de abertura do estabelecimento.", example = "09:00:00")
    private LocalTime openingTime;

    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @Schema(description = "Horário de fechamento do estabelecimento.", example = "17:00:00")
    private LocalTime closingTime;

    /**
     * Atualiza os detalhes do horário de funcionamento com as informações de outro horário.
     *
     * @param workingHours objeto {@link WorkingHours} contendo as informações a serem atualizadas
     * @return referência ao horário de funcionamento atualizado
     */
    public WorkingHours update(@NotNull WorkingHours workingHours) {
        setIfNotNull(this::setDaysOfWeek, workingHours.getDaysOfWeek());
        setIfNotNull(this::setOpeningTime, workingHours.getOpeningTime());
        setIfNotNull(this::setClosingTime, workingHours.getClosingTime());
        return this;
    }
}
