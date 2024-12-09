package br.petservice.backend.service;

import br.petservice.backend.model.Pet;
import br.petservice.backend.repository.PetRepository;
import br.petservice.backend.service.interfaces.PetService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import static br.petservice.backend.enums.ExceptionMessagesEnum.MSG_PET_NOT_FOUND_EXCEPTION;
import static br.petservice.backend.util.SecurityUtil.getAuthenticatedPetGuardian;

/**
 * Implementação do serviço para gerenciar entidades do tipo {@link Pet}.
 * Esta classe oferece operações CRUD para pets, utilizando o repositório {@link PetRepository}.
 *
 * <p>O serviço está integrado com o Spring Security para garantir que o guardião autenticado
 * seja associado ao pet durante o processo de criação.</p>
 */
@Service
@AllArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    /**
     * Busca um pet pelo seu ID.
     *
     * @param id ID do pet a ser encontrado.
     * @return O pet encontrado.
     * @throws EntityNotFoundException Se o pet não for encontrado.
     */
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Pet findById(Long id) {
        return petRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(MSG_PET_NOT_FOUND_EXCEPTION.getMessage()));
    }

    /**
     * Retorna uma página de todos os pets registrados.
     * Requer permissão de administrador.
     *
     * @param index        Índice da página.
     * @param itemsPerPage Número de itens por página.
     * @return Página contendo pets.
     */
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Page<Pet> findAll(int index, int itemsPerPage) {
        return petRepository.findAll(PageRequest.of(index, itemsPerPage));
    }

    /**
     * Retorna uma página com todos os pets registrados de um determinado tutor.
     * Esse método só pode ser acessado por usuários administradores, ou caso o tutor que busca a requisição
     * corresponda ao id passado.
     *
     * @param id           id do tutor.
     * @param index        Índice da página.
     * @param itemsPerPage Número de itens por página.
     * @return Página contendo pets.
     */
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #id == authentication.principal.id")
    public Page<Pet> findAllByPetGuardianId(Long id, int index, int itemsPerPage) {
        return petRepository.findAllByPetGuardianId(id, PageRequest.of(index, itemsPerPage)).orElse(null);
    }

    /**
     * Verifica se o id do pet está vinculado ao usuário atualmente autenticado.
     *
     * @param petId id do pet.
     * @return boolean indicando se o pet está ou nâo vinculado ao usuário atualmente autenticado.
     */
    @Override
    public boolean petGuardianHasPet(Long petId) {

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException(MSG_PET_NOT_FOUND_EXCEPTION.getMessage()));

        return pet.getPetGuardian().getId().equals(getAuthenticatedPetGuardian().getId());
    }

    /**
     * Cria um novo pet e associa-o ao guardião autenticado.
     *
     * @param entity Entidade do pet a ser criada.
     * @return O pet criado.
     */
    @Override
    public Pet create(@NotNull Pet entity) {

        entity.setPetGuardian(getAuthenticatedPetGuardian());

        return petRepository.save(entity);
    }

    /**
     * Atualiza as informações de um pet existente.
     *
     * @param id     ID do pet a ser atualizado.
     * @param entity Entidade com as novas informações.
     * @return O pet atualizado.
     */
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @petServiceImpl.petGuardianHasPet(#id)")
    public Pet update(Long id, Pet entity) {

        Pet pet = findById(id);

        return petRepository.save(pet.update(entity));
    }

    /**
     * Deleta um pet com base no ID fornecido.
     *
     * @param id ID do pet a ser deletado.
     */
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @petServiceImpl.petGuardianHasPet(#id)")
    public void delete(Long id) {

        findById(id);

        petRepository.deleteById(id);
    }
}