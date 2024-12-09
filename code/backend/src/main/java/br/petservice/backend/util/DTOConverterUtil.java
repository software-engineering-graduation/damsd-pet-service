package br.petservice.backend.util;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import br.petservice.backend.controller.PetEstablishmentControllerImpl;
import br.petservice.backend.controller.PetGuardianControllerImpl;
import br.petservice.backend.controller.ServiceProvidedControllerImpl;
import br.petservice.backend.model.Pet;
import br.petservice.backend.model.ServiceProvided;
import br.petservice.backend.model.abstracts.BaseEntity;
import br.petservice.backend.model.abstracts.LegalEntity;
import br.petservice.backend.model.abstracts.NaturalPerson;
import br.petservice.backend.model.abstracts.User;
import br.petservice.backend.model.dto.*;
import java.util.List;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;

/**
 * Classe utilitária responsável por converter entidades em seus respectivos DTOs. Esta classe
 * oferece métodos para converter um {@link User} em {@link PetGuardianDto}, {@link
 * PetEstablishmentDto}, {@link PetDto} e {@link LoginResponseDto}.
 */
@UtilityClass
public class DTOConverterUtil {

    /**
     * Converte uma entidade do tipo {@link NaturalPerson} em um DTO de guardião de pets.
     *
     * @param user Usuário de referência que será convertido.
     * @return {@link PetGuardianDto} contendo as informações do usuário.
     */
    public static PetGuardianDto toDto(@NotNull NaturalPerson user) {
        return PetGuardianDto.builder()
                .id(user.getId())
                .address(user.getAddress())
                .role(user.getRole())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .birthDate(user.getBirthDate())
                .image(user.getImage())
                .links(
                        singletonList(
                                linkTo(PetGuardianControllerImpl.class).slash(user.getId()).withSelfRel()))
                .build();
    }

    /**
     * Converte uma entidade do tipo {@link LegalEntity} em um DTO de estabelecimento de pets.
     *
     * @param user Usuário de referência que será convertido.
     * @return {@link PetEstablishmentDto} contendo as informações do estabelecimento.
     */
    public static PetEstablishmentDto toDto(@NotNull LegalEntity user) {
        return PetEstablishmentDto.builder()
                .id(user.getId())
                .address(user.getAddress())
                .role(user.getRole())
                .email(user.getEmail())
                .businessName(user.getBusinessName())
                .cnpj(user.getCnpj())
                .phoneNumber(user.getPhoneNumber())
                .workingHours(user.getWorkingHours())
                .image(user.getImage())
                .links(
                        singletonList(
                                linkTo(PetEstablishmentControllerImpl.class).slash(user.getId()).withSelfRel()))
                .build();
    }

    /**
     * Converte um usuário autenticado em um DTO de resposta de login.
     *
     * @param user Usuário autenticado que será convertido.
     * @param token Token JWT gerado para o usuário.
     * @param expiration Tempo de expiração do token em milissegundos.
     * @return {@link LoginResponseDto} contendo o ID, nome de usuário, token e o tempo de expiração.
     */
    public static LoginResponseDto toDto(@NotNull User user, String token, long expiration) {
        return LoginResponseDto.builder()
                .id(user.getId())
                .role(user.getRole())
                .username(user.getUsername())
                .token(token)
                .expiresIn(expiration)
                .build();
    }

    /**
     * Converte uma entidade {@link Pet} em um DTO de pet.
     *
     * @param pet Entidade de pet que será convertida.
     * @return {@link PetDto} contendo as informações do pet.
     */
    public static PetDto toDto(@NotNull Pet pet) {
        return PetDto.builder()
                .id(pet.getId())
                .name(pet.getName())
                .race(pet.getRace())
                .specie(pet.getSpecie())
                .weight(pet.getWeight())
                .birthDate(pet.getBirthDate())
                .image(pet.getImage())
                .build();
    }

    /**
     * Converte uma entidade {@link ServiceProvided} em um DTO de serviço prestado.
     *
     * @param serviceProvided Entidade de pet que será convertida.
     * @return {@link ServiceProvided} contendo as informações do serviço prestado.
     */
    public static ServiceProvidedDto toDto(@NotNull ServiceProvided serviceProvided) {
        return ServiceProvidedDto.builder()
                .id(serviceProvided.getId())
                .name(serviceProvided.getName())
                .petEstablishment(serviceProvided.getPetEstablishment())
                .value(serviceProvided.getValue())
                .categoryName(serviceProvided.getCategoryTag().getCategoryName())
                .description(serviceProvided.getDescription())
                .averageRating(serviceProvided.getAverageRating())
                .links(
                        singletonList(
                                linkTo(ServiceProvidedControllerImpl.class)
                                        .slash(serviceProvided.getId())
                                        .withSelfRel()))
                .build();
    }

    public static List<Long> getReferenceIds(Page<? extends BaseEntity> entities) {
        return entities == null ? emptyList() : getReferenceIds(entities.getContent());
    }

    public static List<Long> getReferenceIds(List<? extends BaseEntity> entities) {
        return entities == null
                ? emptyList()
                : entities.stream().filter(Objects::nonNull).map(BaseEntity::getId).distinct().toList();
    }
}
