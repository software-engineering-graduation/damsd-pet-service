package br.petservice.backend.service;

import br.petservice.backend.model.CategoryTag;
import br.petservice.backend.repository.CategoryTagRepository;
import br.petservice.backend.service.interfaces.CategoryTagService;
import br.petservice.backend.service.interfaces.CrudService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.petservice.backend.enums.ExceptionMessagesEnum.MSG_CATEGORY_TAG_FOUND_EXCEPTION;

/**
 * Implementação do serviço responsável por gerenciar as tags de categoria {@link CategoryTag}.
 * Esta classe implementa a interface {@link CrudService} e oferece operações de CRUD (criação, leitura,
 * atualização e exclusão) para as tags de categoria.
 * <p>
 * Os métodos deste serviço requerem que o usuário possua a autoridade 'ROLE_ADMIN' para execução, garantindo
 * que apenas administradores possam gerenciar as tags de categoria.
 */
@Service
@AllArgsConstructor
public class CategoryTagServiceImpl implements CategoryTagService {

    private final CategoryTagRepository categoryTagRepository;

    /**
     * Busca uma tag de categoria pelo ID.
     *
     * @param id ID da tag de categoria a ser buscada.
     * @return A tag de categoria encontrada.
     * @throws EntityNotFoundException Se a tag de categoria não for encontrada.
     */
    @Override
    public CategoryTag findById(Long id) {
        return categoryTagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MSG_CATEGORY_TAG_FOUND_EXCEPTION.getMessage()));
    }

    /**
     * Busca todas as tags de categoria com paginação.
     *
     * @param index        Índice da página a ser retornada.
     * @param itemsPerPage Número de itens por página.
     * @return Uma página contendo as tags de categoria.
     */
    @Override
    public Page<CategoryTag> findAll(int index, int itemsPerPage) {
        return categoryTagRepository.findAll(PageRequest.of(index, itemsPerPage));
    }

    /**
     * Cria uma nova tag de categoria.
     *
     * @param entity A tag de categoria a ser criada.
     * @return A tag de categoria criada.
     */
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CategoryTag create(CategoryTag entity) {
        return categoryTagRepository.save(entity);
    }

    /**
     * Atualiza uma tag de categoria existente pelo ID.
     *
     * @param id     ID da tag de categoria a ser atualizada.
     * @param entity Dados atualizados da tag de categoria.
     * @return A tag de categoria atualizada.
     * @throws EntityNotFoundException Se a tag de categoria não for encontrada.
     */
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CategoryTag update(Long id, CategoryTag entity) {

        findById(id);

        return categoryTagRepository.save(entity);
    }

    /**
     * Deleta uma tag de categoria pelo ID.
     *
     * @param id ID da tag de categoria a ser deletada.
     * @throws EntityNotFoundException Se a tag de categoria não for encontrada.
     */
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(Long id) {

        findById(id);

        categoryTagRepository.deleteById(id);
    }

    /**
     * Verifica se a lista de tags de categorias existe
     *
     * @param categories categorias de referência
     * @return lista de categorias encontradas
     */
    @Override
    public List<CategoryTag> findByCategoryTagNameIn(List<String> categories) {
        return categoryTagRepository.findByCategoryNameIn(categories);
    }
}
