package br.petservice.backend.controller;

import br.petservice.backend.controller.interfaces.CrudController;
import br.petservice.backend.model.CategoryTag;
import br.petservice.backend.service.interfaces.CategoryTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
 * Implementação do controlador responsável por gerenciar as tags de categoria.
 * Esta classe implementa a interface {@link CrudController} e fornece operações de CRUD para a entidade {@link CategoryTag}.
 */
@Slf4j
@RestController
@Validated
@RequestMapping(value = "/category-tag")
@AllArgsConstructor
@Tag(name = "CategoryTagControllerImpl", description = "API para operações de tag de categoria.")
public class CategoryTagControllerImpl implements CrudController<CategoryTag, CategoryTag> {

    private final CategoryTagService categoryTagService;

    /**
     * Busca uma tag de categoria pelo ID fornecido.
     *
     * @param id ID da tag de categoria a ser buscada.
     * @return {@link ResponseEntity} contendo a tag de categoria encontrada.
     */
    @Override
    @Operation(summary = "Buscar uma tag de categoria pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag de categoria encontrada"),
            @ApiResponse(responseCode = "404", description = "Tag de categoria não encontrada")
    })
    public ResponseEntity<CategoryTag> findById(Long id) {

        log.info("findById() - recebendo solicitação para buscar tag de categoria por ID: {}.", id);

        return ResponseEntity.ok(categoryTagService.findById(id));
    }

    /**
     * Retorna todas as tags de categoria com suporte a paginação.
     *
     * @param index        Índice da página.
     * @param itemsPerPage Número de itens por página.
     * @return {@link ResponseEntity} contendo uma página de tags de categoria.
     */
    @Override
    @Operation(summary = "Buscar todas as tags de categoria com paginação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tags de categoria retornada")
    })
    public ResponseEntity<Page<CategoryTag>> findAll(int index, int itemsPerPage) {

        log.info("findAll() - recebendo solicitação para listar todas as tags de categoria.");

        return ResponseEntity.ok(categoryTagService.findAll(index, itemsPerPage));
    }

    /**
     * Cria uma nova tag de categoria.
     *
     * @param entity Objeto representando a nova tag de categoria a ser criada.
     * @return {@link ResponseEntity} contendo a tag de categoria criada.
     */
    @Override
    @Operation(summary = "Criar uma nova tag de categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag de categoria criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    public ResponseEntity<CategoryTag> create(@RequestBody CategoryTag entity) {

        log.info("create() - recebendo solicitação para criar nova tag de categoria.");

        return ResponseEntity.ok(categoryTagService.create(entity));
    }

    /**
     * Atualiza uma tag de categoria pelo ID fornecido.
     *
     * @param id     ID da tag de categoria a ser atualizada.
     * @param entity Objeto contendo os novos dados da tag de categoria.
     * @return {@link ResponseEntity} contendo a tag de categoria atualizada.
     */
    @Override
    @Operation(summary = "Atualizar uma tag de categoria pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag de categoria atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tag de categoria não encontrada"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    public ResponseEntity<CategoryTag> update(Long id, @RequestBody CategoryTag entity) {

        log.info("update() - recebendo solicitação para atualizar a tag de categoria com ID: {}.", id);

        return ResponseEntity.ok(categoryTagService.update(id, entity));
    }

    /**
     * Deleta uma tag de categoria pelo ID fornecido.
     *
     * @param id ID da tag de categoria a ser deletada.
     * @return {@link ResponseEntity} sem conteúdo com status HTTP 204 (No Content).
     */
    @Override
    @Operation(summary = "Deletar uma tag de categoria pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tag de categoria deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tag de categoria não encontrada")
    })
    public ResponseEntity<Void> delete(Long id) {

        log.info("delete() - recebendo solicitação para deletar tag de categoria com ID: {}.", id);

        categoryTagService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
