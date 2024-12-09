package br.petservice.backend.controller;

import br.petservice.backend.controller.interfaces.CrudController;
import br.petservice.backend.controller.interfaces.PetController;
import br.petservice.backend.model.Pet;
import br.petservice.backend.model.dto.PetDto;
import br.petservice.backend.service.interfaces.CrudService;
import br.petservice.backend.service.interfaces.PetService;
import br.petservice.backend.util.DTOConverterUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.petservice.backend.util.DTOConverterUtil.toDto;
import static org.springframework.http.HttpStatus.CREATED;

/**
 * Implementação do controlador responsável por gerenciar os pets.
 * Esta classe implementa a interface {@link CrudController} e fornece operações de CRUD para a entidade {@link Pet}.
 * Utiliza o serviço {@link CrudService} para realizar as operações de manipulação dos dados dos pets.
 */
@Slf4j
@RestController
@Validated
@RequestMapping(value = "/pet")
@AllArgsConstructor
@Tag(name = "PetController", description = "API para operações de pet.")
public class PetControllerImpl implements PetController {

    private final PetService petService;

    /**
     * Retorna as informações de um pet com base no ID fornecido.
     *
     * @param id ID do pet a ser buscado.
     * @return {@link ResponseEntity} contendo o DTO do pet encontrado.
     */
    @Override
    @Operation(summary = "Buscar pet por ID",
               description = "Retorna os detalhes do pet baseado no ID fornecido.")
    public ResponseEntity<PetDto> findById(Long id) {

        log.info("findById() - recebendo solicitação para buscar informações do pet com id: {}.", id);

        return ResponseEntity.ok(toDto(petService.findById(id)));
    }

    /**
     * Retorna uma página de pets com paginação baseada no índice e número de itens por página.
     *
     * @param index        Índice da página.
     * @param itemsPerPage Número de itens por página.
     * @return {@link ResponseEntity} contendo uma página de pets em formato DTO.
     */
    @Override
    @Operation(summary = "Listar todos os pets",
               description = "Retorna uma lista paginada de todos os pets.")
    public ResponseEntity<Page<PetDto>> findAll(int index, int itemsPerPage) {

        log.info("findAll() - recebendo solicitação para buscar todos os pets.");

        return ResponseEntity.ok(petService.findAll(index, itemsPerPage).map(DTOConverterUtil::toDto));
    }

    /**
     * Retorna uma página de pets pertencentes ao guardião especificado pelo ID.
     *
     * @param id           ID do guardião de pets.
     * @param index        Índice da página.
     * @param itemsPerPage Número de itens por página.
     * @return {@link ResponseEntity} contendo uma página de pets pertencentes ao guardião em formato DTO.
     */
    @Override
    @Operation(summary = "Listar pets por guardião",
               description = "Retorna uma lista paginada de pets pertencentes ao guardião especificado.")
    public ResponseEntity<Page<PetDto>> findAllByPetGuardianId(Long id, int index, int itemsPerPage) {

        log.info("findAllByPetGuardianId() - recebendo solicitação para buscar todos os pets do guardião com id: {}.", id);

        return ResponseEntity.ok(petService.findAllByPetGuardianId(id, index, itemsPerPage).map(DTOConverterUtil::toDto));
    }

    /**
     * Registra um novo pet.
     *
     * @param entity Objeto contendo os dados do pet a ser registrado.
     * @return {@link ResponseEntity} contendo o DTO do pet registrado e o status HTTP 201 (Created).
     */
    @Override
    @Operation(summary = "Registrar um novo pet",
               description = "Registra um novo pet.")
    public ResponseEntity<PetDto> create(Pet entity) {

        log.info("create() - recebendo solicitação para registrar pet: {}.", entity);

        return ResponseEntity.status(CREATED).body(DTOConverterUtil.toDto(petService.create(entity)));
    }

    /**
     * Atualiza as informações de um pet existente.
     *
     * @param id     ID do pet a ser atualizado.
     * @param entity Objeto contendo as novas informações do pet.
     * @return {@link ResponseEntity} contendo o DTO do pet atualizado.
     */
    @Override
    @Operation(summary = "Atualizar um pet existente",
               description = "Atualiza as informações de um pet existente.")
    public ResponseEntity<PetDto> update(Long id, Pet entity) {

        log.info("update() - recebendo solicitação para atualizar o pet com id: {}.", id);

        return ResponseEntity.ok().body(DTOConverterUtil.toDto(petService.update(id, entity)));
    }

    /**
     * Deleta um pet com base no ID fornecido.
     *
     * @param id ID do pet a ser deletado.
     * @return {@link ResponseEntity} sem conteúdo com status HTTP 204 (No Content).
     */
    @Override
    @Operation(summary = "Deletar um pet",
               description = "Deleta um pet existente baseado no ID fornecido.")
    public ResponseEntity<Void> delete(Long id) {

        log.info("delete() - recebendo solicitação para deletar o pet com id: {}.", id);

        petService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
