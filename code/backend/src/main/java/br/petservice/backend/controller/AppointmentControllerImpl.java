package br.petservice.backend.controller;

import br.petservice.backend.model.Appointment;
import br.petservice.backend.model.dto.AppointmentDto;
import br.petservice.backend.model.dto.AppointmentRequestDTO;
import br.petservice.backend.service.AppointmentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping(value = "/appointment")
@AllArgsConstructor
@Tag(name = "AppointmentControllerImpl", description = "API para operações de agendamento.")
public class AppointmentControllerImpl {

    private final AppointmentServiceImpl appointmentService;

    /**
     * Recupera um agendamento pelo seu ID.
     *
     * @param id o ID do agendamento a ser recuperado
     * @return o agendamento com o ID especificado
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar agendamento por ID", description = "Recupera um agendamento pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento recuperado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    })
    public ResponseEntity<AppointmentDto> findById(@PathVariable Long id) {
        log.info("findById() - recebendo solicitação para buscar agendamento por ID: {}.", id);
        return ResponseEntity.ok(appointmentService.convertToDto(appointmentService.findById(id)));
    }

    /**
     * Recupera todos os agendamentos de um determinado guardião de pets.
     *
     * @param id o ID do guardião de pets
     * @return uma lista de agendamentos do guardião de pets
     */
    @GetMapping("/pet-guardian/{id}")
    @Operation(summary = "Listar agendamentos por ID do guardião de pets", description = "Recupera todos os agendamentos de um determinado guardião de pets.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamentos recuperados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Guardião de pets não encontrado")
    })
    public ResponseEntity<List<AppointmentDto>> findAllByPetGuardianId(@PathVariable Long id) {
        log.info(
                "findAllByPetGuardianId() - recebendo solicitação para listar agendamentos por ID do guardião de pets: {}.",
                id);
        return ResponseEntity.ok(appointmentService.findAllByPetGuardianId(id).stream().map(appointmentService::convertToDto).toList());
    }

    /**
     * Recupera todos os agendamentos de um determinado estabelecimento de pets.
     *
     * @param id o ID do estabelecimento de pets
     * @return uma lista de agendamentos do estabelecimento de pets
     */
    @GetMapping("/pet-establishment/{id}")
    @Operation(summary = "Listar agendamentos por ID do estabelecimento de pets", description = "Recupera todos os agendamentos de um determinado estabelecimento de pets.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamentos recuperados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Estabelecimento de pets não encontrado")
    })
    public ResponseEntity<List<AppointmentDto>> findAllByPetEstablishmentId(@PathVariable Long id) {
        log.info(
                "findAllByPetEstablishmentId() - recebendo solicitação para listar agendamentos por ID do estabelecimento de pets: {}.",
                id);

        return ResponseEntity.ok(appointmentService.findAllByPetEstablishmentId(id).stream().map(appointmentService::convertToDto).toList());
    }

    /**
     * Recupera todos os agendamentos com paginação.
     *
     * @param index        o índice da página
     * @param itemsPerPage o número de itens por página
     * @return uma página de agendamentos
     */
    @GetMapping
    @Operation(summary = "Listar todos os agendamentos", description = "Recupera todos os agendamentos com paginação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamentos recuperados com sucesso")
    })
    public ResponseEntity<Page<AppointmentDto>> findAll(@RequestParam int index, @RequestParam int itemsPerPage) {
        log.info("findAll() - recebendo solicitação para listar todos os agendamentos.");
        return ResponseEntity.ok(appointmentService.findAll(index, itemsPerPage).map(appointmentService::convertToDto));
    }

    /**
     * Cria um novo agendamento.
     *
     * @param entity os dados do agendamento a ser criado
     * @return o agendamento criado
     */
    @PostMapping
    @Operation(summary = "Criar um novo agendamento", description = "Cria um novo agendamento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Agendamento criado com sucesso")
    })
    public ResponseEntity<AppointmentDto> create(@RequestBody AppointmentRequestDTO entity) {
        log.info("create() - recebendo solicitação para criar um novo agendamento.");
        return ResponseEntity.status(201).body(appointmentService.convertToDto(appointmentService.create(entity)));
    }

    /**
     * Atualiza um agendamento existente.
     *
     * @param id     o ID do agendamento a ser atualizado
     * @param entity os dados atualizados do agendamento
     * @return o agendamento atualizado
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um agendamento", description = "Atualiza um agendamento existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    })
    public ResponseEntity<AppointmentDto> update(@PathVariable Long id, @RequestBody AppointmentRequestDTO entity) {
        log.info("update() - recebendo solicitação para atualizar o agendamento com ID: {}.", id);
        return ResponseEntity.ok(appointmentService.convertToDto(appointmentService.update(id, entity)));
    }

    /**
     * Deleta um agendamento pelo seu ID.
     *
     * @param id o ID do agendamento a ser deletado
     * @return uma entidade de resposta sem conteúdo
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um agendamento", description = "Deleta um agendamento pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Agendamento deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("delete() - recebendo solicitação para deletar agendamento com ID: {}.", id);
        appointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}