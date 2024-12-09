package br.petservice.backend.model;

import br.petservice.backend.model.abstracts.NaturalPerson;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.List;

import static br.petservice.backend.util.SetUtil.setIfNotNull;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static jakarta.persistence.CascadeType.PERSIST;

@Data
@Entity
@Table(name = "pet_guardians")
@EqualsAndHashCode(callSuper = true)
public class PetGuardian extends NaturalPerson {

    @Valid
    @ToString.Exclude
    @OneToMany(mappedBy = "petGuardian", cascade = PERSIST)
    @JsonProperty(access = READ_ONLY)
    @JsonIgnore
    private List<Pet> pets;

    public PetGuardian update(@NotNull PetGuardian petGuardian) {
        setIfNotNull(this::setFullName, petGuardian.getFullName());
        setIfNotNull(this::setPassword, petGuardian.getPassword());
        setIfNotNull(this::setCpf, petGuardian.getCpf());
        setIfNotNull(this::setBirthDate, petGuardian.getBirthDate());
        setIfNotNull(this::setAddress, petGuardian.getAddress());
        setIfNotNull(this::setPhoneNumber, petGuardian.getPhoneNumber());
        setIfNotNull(this::setEmail, petGuardian.getEmail());
        setIfNotNull(this::setUpdatedAt, LocalDateTime.now());
        setIfNotNull(this::setImage, petGuardian.getImage());
        return this;
    }
}
