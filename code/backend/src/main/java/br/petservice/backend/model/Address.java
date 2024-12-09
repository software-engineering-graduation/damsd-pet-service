package br.petservice.backend.model;

import br.petservice.backend.model.abstracts.BaseEntity;
import br.petservice.backend.model.abstracts.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import static br.petservice.backend.util.SetUtil.setIfNotNull;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "addresses")
@EqualsAndHashCode(callSuper = true)
public class Address extends BaseEntity {

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "address")
    @JsonProperty(access = WRITE_ONLY)
    private User user;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private int streetNumber;

    @Column(nullable = false)
    private String neighborhood;

    @Column(name = "additional_information")
    private String additionalInformation;

    @Column(name = "postal_code", nullable = false)
    @Pattern(regexp = "^[0-9]{8}$", message = "formato esperado: 8 dígitos numéricos consecutivos")
    private String postalCode;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "uf", nullable = false)
    private String uf;

    @Column(name = "region", nullable = false)
    private String region;

    public Address update(@NotNull Address address) {
        setIfNotNull(this::setPostalCode, address.getPostalCode());
        setIfNotNull(this::setStreet, address.getStreet());
        setIfNotNull(this::setStreetNumber, address.getStreetNumber());
        setIfNotNull(this::setNeighborhood, address.getNeighborhood());
        setIfNotNull(this::setCity, address.getCity());
        setIfNotNull(this::setState, address.getState());
        setIfNotNull(this::setUf, address.getUf());
        setIfNotNull(this::setRegion, address.getRegion());
        setIfNotNull(this::setAdditionalInformation, address.getAdditionalInformation());
        return this;
    }
}
