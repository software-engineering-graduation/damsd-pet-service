package br.petservice.backend.service;

import static br.petservice.backend.enums.ExceptionMessagesEnum.MSG_CATEGORY_TAG_NOT_FOUND_EXCEPTION;
import static br.petservice.backend.enums.ExceptionMessagesEnum.MSG_SERVICE_PROVIDED_NOT_FOUND_EXCEPTION;
import static br.petservice.backend.util.Constants.INDEX_ZERO;
import static br.petservice.backend.util.DTOConverterUtil.getReferenceIds;
import static br.petservice.backend.util.RatingUtil.fillRating;
import static br.petservice.backend.util.SecurityUtil.getAuthenticatedPetEstablishment;
import static java.lang.String.format;
import static java.util.Collections.singletonList;

import br.petservice.backend.model.CategoryTag;
import br.petservice.backend.model.PetEstablishment;
import br.petservice.backend.model.Review;
import br.petservice.backend.model.ServiceProvided;
import br.petservice.backend.repository.ServiceProvidedRepository;
import br.petservice.backend.service.interfaces.CategoryTagService;
import br.petservice.backend.service.interfaces.ReviewService;
import br.petservice.backend.service.interfaces.ServiceProvidedService;
import br.petservice.backend.service.interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 * Implementação do serviço para gerenciar entidades do tipo {@link ServiceProvided}. Esta classe
 * oferece operações CRUD para serviços prestados, utilizando o repositório {@link
 * ServiceProvidedRepository}.
 *
 * <p>O serviço está integrado com o Spring Security para garantir que o estabelecimento autenticado
 * seja associado ao serviço durante o processo de criação.
 */
@Service
@AllArgsConstructor
public class ServiceProvidedServiceImpl implements ServiceProvidedService {

    private final CategoryTagService categoryTagService;
    private final UserService<PetEstablishment> petEstablishmentService;
    private final ServiceProvidedRepository serviceProvidedRepository;
    private final ReviewService reviewService;

    /**
     * Busca um serviço prestado pelo seu ID.
     *
     * @param id ID do serviço a ser encontrado.
     * @return O serviço prestado encontrado.
     * @throws EntityNotFoundException Se o serviço não for encontrado.
     */
    @Override
    public ServiceProvided findById(Long id) {
        return serviceProvidedRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new EntityNotFoundException(MSG_SERVICE_PROVIDED_NOT_FOUND_EXCEPTION.getMessage()));
    }

    /**
     * Retorna uma página de todos os serviços prestados registrados. Requer permissão de
     * administrador.
     *
     * @param index Índice da página.
     * @param itemsPerPage Número de itens por página.
     * @return Página contendo serviços prestados.
     */
    @Override
    public Page<ServiceProvided> findAll(int index, int itemsPerPage) {
        Page<ServiceProvided> servicesProvided =
                serviceProvidedRepository.findAll(PageRequest.of(index, itemsPerPage));
        List<PetEstablishment> petEstablishments =
                servicesProvided.map(ServiceProvided::getPetEstablishment).stream()
                        .toList();
        List<Review> reviews =
                reviewService.findAllByReviewedUserIds(getReferenceIds(petEstablishments));

        fillRating(petEstablishments, reviews);
        return servicesProvided;
    }

    /**
     * Retorna uma página de todos os serviços prestados registrados de um estabelecimento. Requer
     * permissão de administrador.
     *
     * @param id id do estabelecimento de referência.
     * @param index Índice da página.
     * @param itemsPerPage Número de itens por página.
     * @return Página contendo serviços prestados.
     */
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #id == authentication.principal.id")
    public Page<ServiceProvided> findAllByPetEstablishmentId(Long id, int index, int itemsPerPage) {
        return serviceProvidedRepository.findAllByPetEstablishmentId(id, PageRequest.of(index, itemsPerPage)).orElse(
                null);
    }

    /**
     * Cria um novo serviço prestado e associa-o ao estabelecimento autenticado.
     *
     * @param serviceProvided Entidade do serviço a ser criada.
     * @return O serviço criado.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ServiceProvided create(ServiceProvided serviceProvided) {
        return create(singletonList(serviceProvided)).get(INDEX_ZERO);
    }

    /**
     * Cria uma lista de serviços prestados e associa-os ao estabelecimento autenticado.
     *
     * @param servicesProvided Lista de entidades de serviços a serem criadas.
     * @return Lista de serviços criados.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public List<ServiceProvided> create(@NotNull List<ServiceProvided> servicesProvided) {

        PetEstablishment petEstablishment =
                petEstablishmentService.findById(getAuthenticatedPetEstablishment().getId());

        servicesProvided.forEach(
                serviceProvided -> {
                    List<CategoryTag> categoryTagList =
                            categoryTagService.findByCategoryTagNameIn(
                                    singletonList(serviceProvided.getCategoryTag().getCategoryName()));

                    if (categoryTagList.isEmpty())
                        throw new EntityNotFoundException(
                                format(
                                        MSG_CATEGORY_TAG_NOT_FOUND_EXCEPTION.getMessage(),
                                        serviceProvided.getCategoryTag().getCategoryName()));

                    serviceProvided.setPetEstablishment(petEstablishment);
                    serviceProvided.setCategoryTag(categoryTagList.get(INDEX_ZERO));
                });
        return serviceProvidedRepository.saveAll(servicesProvided);
    }

    /**
     * Atualiza as informações de um serviço existente.
     *
     * @param id ID do serviço a ser atualizado.
     * @param entity Entidade com as novas informações.
     * @return O serviço atualizado.
     */
    @Override
    @PreAuthorize(
            "hasAuthority('ROLE_ADMIN') or @serviceProvidedServiceImpl.petEstablishmentHasServiceProvided(#id)")
    public ServiceProvided update(Long id, ServiceProvided entity) {

        ServiceProvided serviceProvided = findById(id);
        List<CategoryTag> categoryTagList =
                categoryTagService.findByCategoryTagNameIn(
                        singletonList(entity.getCategoryTag().getCategoryName()));
        CategoryTag categoryTag = new CategoryTag();
        categoryTag.setId(categoryTagList.get(INDEX_ZERO).getId());
        entity.setCategoryTag(categoryTag);

        return serviceProvidedRepository.save(serviceProvided.update(entity));
    }

    /**
     * Deleta um serviço com base no ID fornecido.
     *
     * @param id ID do serviço a ser deletado.
     */
    @Override
    @PreAuthorize(
            "hasAuthority('ROLE_ADMIN') or @serviceProvidedServiceImpl.petEstablishmentHasServiceProvided(#id)")
    public void delete(Long id) {

        findById(id);

        serviceProvidedRepository.deleteById(id);
    }

    /**
     * Verifica se o id do serviço está vinculado ao estabelecimento atualmente autenticado.
     *
     * @param serviceProvidedId id do serviço.
     * @return boolean indicando se o serviço está ou nâo vinculado ao estabelecimento atualmente
     *     autenticado.
     */
    @Override
    public boolean petEstablishmentHasServiceProvided(Long serviceProvidedId) {

        ServiceProvided serviceProvided =
                serviceProvidedRepository
                        .findById(serviceProvidedId)
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                MSG_SERVICE_PROVIDED_NOT_FOUND_EXCEPTION.getMessage()));

        return serviceProvided
                .getPetEstablishment()
                .getId()
                .equals(getAuthenticatedPetEstablishment().getId());
    }
}
