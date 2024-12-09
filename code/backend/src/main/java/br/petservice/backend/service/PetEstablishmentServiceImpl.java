package br.petservice.backend.service;

import br.petservice.backend.model.PetEstablishment;
import br.petservice.backend.model.Review;
import br.petservice.backend.model.dto.LoginUserDto;
import br.petservice.backend.repository.UserRepository;
import br.petservice.backend.service.interfaces.AuthenticationService;
import br.petservice.backend.service.interfaces.ReviewService;
import br.petservice.backend.service.interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.petservice.backend.enums.ExceptionMessagesEnum.MSG_USER_NOT_FOUND_EXCEPTION;
import static br.petservice.backend.util.DTOConverterUtil.getReferenceIds;
import static br.petservice.backend.util.RatingUtil.fillRating;
import static java.util.stream.Collectors.groupingBy;

/**
 * Implementação do serviço para gerenciar usuários do tipo {@link PetEstablishment}.
 * Esta classe oferece operações CRUD e autenticação de usuários, utilizando o repositório
 * {@link UserRepository} e o serviço de autenticação {@link AuthenticationService}.
 */
@Service
@AllArgsConstructor
@Transactional
public class PetEstablishmentServiceImpl implements UserService<PetEstablishment> {

    private final UserRepository<PetEstablishment> userRepository;
    private final AuthenticationService<PetEstablishment> authenticationService;
    private final ReviewService reviewService;

    /**
     * Registra um novo estabelecimento de pets.
     *
     * @param registerDto DTO contendo as informações do novo estabelecimento.
     * @return O estabelecimento de pets recém-registrado.
     */
    @Override
    public PetEstablishment register(@NotNull PetEstablishment registerDto) {
        return authenticationService.register(registerDto);
    }

    /**
     * Autentica um estabelecimento de pets com base nas credenciais de login.
     *
     * @param loginUserDto DTO contendo as credenciais de login.
     * @return O estabelecimento autenticado.
     */
    @Override
    public PetEstablishment authenticate(@NotNull LoginUserDto loginUserDto) {
        return authenticationService.authenticate(loginUserDto);
    }

    /**
     * Busca um estabelecimento de pets pelo seu ID.
     *
     * @param id ID do estabelecimento a ser encontrado.
     * @return O estabelecimento encontrado.
     * @throws EntityNotFoundException Se o estabelecimento não for encontrado.
     */
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #id == authentication.principal.id")
    public PetEstablishment findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MSG_USER_NOT_FOUND_EXCEPTION.getMessage()));
    }

    /**
     * Retorna uma página de todos os estabelecimentos de pets registrados.
     * Requer permissão de administrador.
     *
     * @param index        Índice da página.
     * @param itemsPerPage Número de itens por página.
     * @return Página contendo estabelecimentos de pets.
     */
    @Override
    public Page<PetEstablishment> findAll(int index, int itemsPerPage) {
        Page<PetEstablishment> petEstablishments = userRepository.findAll(PageRequest.of(index, itemsPerPage));
        List<Review> reviews = reviewService.findAllByReviewedUserIds(getReferenceIds(petEstablishments));
        fillRating(petEstablishments.getContent(),  reviews);
        return petEstablishments;
    }

    /**
     * Cria um novo estabelecimento de pets.
     *
     * @param entity Entidade do estabelecimento a ser criado.
     * @return O estabelecimento criado.
     */
    @Override
    public PetEstablishment create(PetEstablishment entity) {
        return register(entity);
    }

    /**
     * Atualiza as informações de um estabelecimento de pets existente.
     *
     * @param id     ID do estabelecimento a ser atualizado.
     * @param entity Entidade com as novas informações.
     * @return O estabelecimento atualizado.
     */
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #id == authentication.principal.id")
    public PetEstablishment update(Long id, PetEstablishment entity) {

        PetEstablishment user = findById(id);

        return userRepository.save(user.update(entity));
    }

    /**
     * Deleta um estabelecimento de pets com base no ID.
     * Requer permissão de administrador.
     *
     * @param id ID do estabelecimento a ser deletado.
     */
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(Long id) {
        findById(id);
        userRepository.deleteById(id);
    }


}
