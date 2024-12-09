package br.petservice.backend.model;

import br.petservice.backend.model.abstracts.LegalEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.List;

import static br.petservice.backend.util.SetUtil.setIfNotNull;

@Data
@Entity
@Table(name = "pet_establishments")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Entidade que representa um Estabelecimento para Pets no sistema")
public class PetEstablishment extends LegalEntity {

    @ToString.Exclude
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "petEstablishment", fetch = FetchType.EAGER)
    @Schema(description = "Lista de serviços prestados pelo estabelecimento")
    private List<ServiceProvided> servicesProvided;

    /**
     * Método para atualizar as informações do estabelecimento de pet.
     *
     * @param petEstablishment Dados do estabelecimento com os valores a serem atualizados
     * @return O estabelecimento de pet atualizado
     */
    public PetEstablishment update(@NotNull PetEstablishment petEstablishment) {
        setIfNotNull(this::setBusinessName, petEstablishment.getBusinessName());
        setIfNotNull(this::setPassword, petEstablishment.getPassword());
        setIfNotNull(this::setCnpj, petEstablishment.getCnpj());
        setIfNotNull(this::setAddress, petEstablishment.getAddress());
        setIfNotNull(this::setServicesProvided, petEstablishment.getServicesProvided());
        setIfNotNull(this::setPhoneNumber, petEstablishment.getPhoneNumber());
        setIfNotNull(this::setEmail, petEstablishment.getEmail());
        setIfNotNull(this::setWorkingHours, petEstablishment.getWorkingHours());
        setIfNotNull(this::setUpdatedAt, LocalDateTime.now());
        setIfNotNull(this::setImage, petEstablishment.getImage());
        return this;
    }
}
