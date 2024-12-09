package br.petservice.backend.service;

import br.petservice.backend.model.Review;
import br.petservice.backend.repository.ReviewRepository;
import br.petservice.backend.service.interfaces.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import static br.petservice.backend.enums.ExceptionMessagesEnum.MSG_REVIEW_FOUND_EXCEPTION;

/**
 * Implementação do serviço de avaliações (Review) para manipulação de operações relacionadas a avaliações
 * no sistema Petservice, como criação, atualização, exclusão e consultas personalizadas.
 * <p>
 * Esta classe é anotada com `@Service`, tornando-a um componente gerenciado pelo Spring para injeção de dependência.
 * `@AllArgsConstructor` é utilizado para injetar automaticamente o repositório de avaliações, enquanto as anotações
 * de segurança `@PreAuthorize` garantem que apenas usuários com permissões apropriadas possam realizar certas operações.
 */
@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    /**
     * Busca uma avaliação pelo seu identificador único (ID).
     *
     * @param id O identificador único da avaliação.
     * @return A avaliação encontrada.
     * @throws EntityNotFoundException Se a avaliação não for encontrada.
     */
    @Override
    public Review findById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MSG_REVIEW_FOUND_EXCEPTION.getMessage()));
    }

    /**
     * Busca todas as avaliações, com suporte para paginação.
     *
     * @param index        Índice da página.
     * @param itemsPerPage Número de itens por página.
     * @return Uma página de avaliações.
     */
    @Override
    public Page<Review> findAll(int index, int itemsPerPage) {
        return reviewRepository.findAll(PageRequest.of(index, itemsPerPage));
    }

    /**
     * Cria uma nova avaliação, disponível para usuários com as permissões de ADMIN ou USER.
     *
     * @param entity A entidade de avaliação a ser criada.
     * @return A avaliação criada.
     */
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public Review create(Review entity) {
        return reviewRepository.save(entity);
    }

    /**
     * Atualiza uma avaliação existente com base no ID fornecido, disponível para usuários com as permissões de ADMIN ou USER.
     *
     * @param id     O identificador único da avaliação a ser atualizada.
     * @param entity A entidade de avaliação com as informações atualizadas.
     * @return A avaliação atualizada.
     */
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public Review update(Long id, Review entity) {
        entity.setId(id);
        return reviewRepository.save(entity);
    }

    /**
     * Exclui uma avaliação com base no ID fornecido.
     * Disponível para ADMINs ou para o autor da avaliação.
     *
     * @param id O identificador único da avaliação a ser excluída.
     */
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #reviewServiceImpl.findById(id).authorId == authentication.principal.id")
    public void delete(Long id) {

        findById(id);

        reviewRepository.deleteById(id);
    }

    /**
     * Busca todas as avaliações feitas por um autor específico, com suporte para paginação.
     *
     * @param userId       O identificador único do autor das avaliações.
     * @param index        Índice da página.
     * @param itemsPerPage Número de itens por página.
     * @return Uma página de avaliações do autor especificado.
     */
    @Override
    public Page<Review> findAllByAuthorId(long userId, int index, int itemsPerPage) {
        return reviewRepository.findAllByAuthorId(userId, PageRequest.of(index, itemsPerPage));
    }

    /**
     * Busca todas as avaliações feitas para os usuários especificados.
     *
     * @param reviewedUserIds Lista de identificadores únicos dos usuários avaliados.
     * @return Uma lista de avaliações dos usuários especificados.
     */
    @Override
    public List<Review> findAllByReviewedUserIds(List<Long> reviewedUserIds) {
        return reviewRepository.findAllByReviewedUserIdIn(reviewedUserIds);
    }

}
