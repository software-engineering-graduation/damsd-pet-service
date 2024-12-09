package br.petservice.backend.service;

import br.petservice.backend.model.PetGuardian;
import br.petservice.backend.model.dto.LoginUserDto;
import br.petservice.backend.repository.UserRepository;
import br.petservice.backend.service.interfaces.AuthenticationService;
import br.petservice.backend.service.interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import static br.petservice.backend.enums.ExceptionMessagesEnum.MSG_USER_NOT_FOUND_EXCEPTION;

/**
 * Implementação do serviço para gerenciar usuários do tipo {@link PetGuardian}.
 * Esta classe oferece operações CRUD e autenticação de usuários, utilizando o repositório
 * {@link UserRepository} e o serviço de autenticação {@link AuthenticationService}.
 */
@Service
@AllArgsConstructor
public class PetGuardianServiceImpl implements UserService<PetGuardian> {

    private final UserRepository<PetGuardian> userRepository;
    private final AuthenticationService<PetGuardian> authenticationService;

    /**
     * Registra um novo guardião de pets.
     *
     * @param registerDto DTO contendo as informações do novo guardião.
     * @return O guardião de pets recém-registrado.
     */
    @Override
    public PetGuardian register(@NotNull PetGuardian registerDto) {
        return authenticationService.register(registerDto);
    }

    /**
     * Autentica um guardião de pets com base nas credenciais de login.
     *
     * @param loginUserDto DTO contendo as credenciais de login.
     * @return O guardião autenticado.
     */
    @Override
    public PetGuardian authenticate(@NotNull LoginUserDto loginUserDto) {
        return authenticationService.authenticate(loginUserDto);
    }

    /**
     * Busca um guardião de pets pelo seu ID.
     *
     * @param id ID do guardião a ser encontrado.
     * @return O guardião encontrado.
     * @throws EntityNotFoundException Se o guardião não for encontrado.
     */
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #id == authentication.principal.id")
    public PetGuardian findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MSG_USER_NOT_FOUND_EXCEPTION.getMessage()));
    }

    /**
     * Retorna uma página de todos os guardiões de pets registrados.
     * Requer permissão de administrador.
     *
     * @param index        Índice da página.
     * @param itemsPerPage Número de itens por página.
     * @return Página contendo guardiões de pets.
     */
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Page<PetGuardian> findAll(int index, int itemsPerPage) {
        return userRepository.findAll(PageRequest.of(index, itemsPerPage));
    }

    /**
     * Cria um novo guardião de pets.
     *
     * @param entity Entidade do guardião a ser criada.
     * @return O guardião criado.
     */
    @Override
    public PetGuardian create(PetGuardian entity) {
        return register(entity);
    }

    /**
     * Atualiza as informações de um guardião de pets existente.
     *
     * @param id     ID do guardião a ser atualizado.
     * @param entity Entidade com as novas informações.
     * @return O guardião atualizado.
     */
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #id == authentication.principal.id")
    public PetGuardian update(Long id, PetGuardian entity) {

        PetGuardian user = findById(id);

        return userRepository.save(user.update(entity));
    }

    /**
     * Deleta um guardião de pets com base no ID.
     * Requer permissão de administrador.
     *
     * @param id ID do guardião a ser deletado.
     */
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(Long id) {

        findById(id);

        userRepository.deleteById(id);
    }
}
