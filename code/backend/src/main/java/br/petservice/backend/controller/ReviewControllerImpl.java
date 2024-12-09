package br.petservice.backend.controller;

import br.petservice.backend.controller.interfaces.ReviewController;
import br.petservice.backend.model.Review;
import br.petservice.backend.service.interfaces.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implementação do controlador de avaliações (Review) da API para operações CRUD e consultas personalizadas
 * relacionadas a avaliações no sistema Petservice.
 * Esta classe é marcada como `RestController`, o que a torna um controlador RESTful no Spring Framework,
 * permitindo o tratamento de requisições HTTP no contexto de avaliações.
 * <p>
 * Anotado com `@Tag` para descrever a classe na documentação gerada pelo Swagger.
 * `@AllArgsConstructor` é utilizado para injetar automaticamente as dependências,
 * enquanto `@Slf4j` adiciona capacidade de log para auxiliar no monitoramento e depuração.
 */
@Slf4j
@RestController
@Validated
@RequestMapping(value = "/review")
@AllArgsConstructor
@Tag(name = "ReviewControllerImpl", description = "API para operações de tag de avaliações.")
public class ReviewControllerImpl implements ReviewController {

    private final ReviewService reviewService;

    /**
     * Busca uma avaliação pelo seu identificador único (ID).
     *
     * @param id O identificador único da avaliação a ser buscada.
     * @return ResponseEntity contendo a avaliação encontrada ou status HTTP apropriado se não encontrada.
     */
    @Override
    public ResponseEntity<Review> findById(Long id) {
        return ResponseEntity.ok(reviewService.findById(id));
    }

    /**
     * Busca todas as avaliações, com suporte para paginação.
     *
     * @param index        Índice da página.
     * @param itemsPerPage Número de itens por página.
     * @return ResponseEntity contendo uma página de avaliações.
     */
    @Override
    public ResponseEntity<Page<Review>> findAll(int index, int itemsPerPage) {
        return ResponseEntity.ok(reviewService.findAll(index, itemsPerPage));
    }

    /**
     * Cria uma nova avaliação.
     *
     * @param entity A entidade de avaliação a ser criada.
     * @return ResponseEntity contendo a avaliação criada.
     */
    @Override
    public ResponseEntity<Review> create(@RequestBody Review entity) {
        return ResponseEntity.ok(reviewService.create(entity));
    }

    /**
     * Atualiza uma avaliação existente com base no ID fornecido.
     *
     * @param id     O identificador único da avaliação a ser atualizada.
     * @param entity A entidade de avaliação com as informações atualizadas.
     * @return ResponseEntity contendo a avaliação atualizada.
     */
    @Override
    public ResponseEntity<Review> update(Long id, Review entity) {
        return ResponseEntity.ok(reviewService.update(id, entity));
    }

    /**
     * Exclui uma avaliação com base no ID fornecido.
     *
     * @param id O identificador único da avaliação a ser excluída.
     * @return ResponseEntity com status HTTP indicando que o recurso foi excluído.
     */
    @Override
    public ResponseEntity<Void> delete(Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca todas as avaliações feitas por um autor específico, com suporte para paginação.
     *
     * @param id           O identificador único do autor das avaliações.
     * @param index        Índice da página.
     * @param itemsPerPage Número de itens por página.
     * @return ResponseEntity contendo uma página de avaliações do autor especificado.
     */
    @Override
    public ResponseEntity<Page<Review>> findAllByAuthorId(Long id, int index, int itemsPerPage) {
        return ResponseEntity.ok(reviewService.findAllByAuthorId(id, index, itemsPerPage));
    }

}

