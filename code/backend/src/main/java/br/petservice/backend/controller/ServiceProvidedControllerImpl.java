package br.petservice.backend.controller;

import br.petservice.backend.controller.interfaces.ServiceProvidedController;
import br.petservice.backend.model.PetEstablishment;
import br.petservice.backend.model.Review;
import br.petservice.backend.model.ServiceProvided;
import br.petservice.backend.model.dto.ServiceProvidedDto;
import br.petservice.backend.service.interfaces.ReviewService;
import br.petservice.backend.service.interfaces.ServiceProvidedService;
import br.petservice.backend.util.DTOConverterUtil;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static br.petservice.backend.util.DTOConverterUtil.getReferenceIds;
import static br.petservice.backend.util.DTOConverterUtil.toDto;
import static br.petservice.backend.util.RatingUtil.fillRating;

/**
 * Implementação do controlador responsável por gerenciar os serviços prestados.
 * Esta classe implementa a interface {@link ServiceProvidedController} e fornece operações de CRUD para a entidade {@link ServiceProvided}.
 * Utiliza o serviço {@link ServiceProvidedService} para realizar as operações de manipulação dos dados dos serviços prestados.
 * <p>
 * O controlador é exposto através do endpoint "/service-provided" e contém operações como buscar, listar, criar, atualizar e deletar serviços.
 * </p>
 */
@Slf4j
@RestController
@RequestMapping(value = "/service-provided")
@AllArgsConstructor
public class ServiceProvidedControllerImpl implements ServiceProvidedController {

    private final ServiceProvidedService serviceProvidedService;

    /**
     * Busca um serviço prestado pelo seu ID.
     *
     * @param id ID do serviço prestado.
     * @return {@link ResponseEntity} contendo o DTO do serviço prestado encontrado.
     */
    @Override
    public ResponseEntity<ServiceProvidedDto> findById(Long id) {

        log.info("findById() - buscando serviço prestado com id: {}", id);

        return ResponseEntity.ok(toDto(serviceProvidedService.findById(id)));
    }

    /**
     * Retorna uma página de serviços prestados com paginação.
     *
     * @param index        Índice da página.
     * @param itemsPerPage Número de itens por página.
     * @return {@link ResponseEntity} contendo uma página de serviços prestados em formato DTO.
     */
    @Override
    public ResponseEntity<Page<ServiceProvidedDto>> findAll(int index, int itemsPerPage) {

        log.info("findAll() - buscando serviços prestados na página {} com {} itens por página.", index, itemsPerPage);

        return ResponseEntity.ok(serviceProvidedService.findAll(index, itemsPerPage).map(DTOConverterUtil::toDto));
    }

    /**
     * Retorna uma página de serviços de prestados de um estabelecimento com paginação.
     *
     * @param id           id do estabelecimento de referência.
     * @param index        Índice da página.
     * @param itemsPerPage Número de itens por página.
     * @return {@link ResponseEntity} contendo uma página de serviços prestados em formato DTO.
     */
    @Override
    public ResponseEntity<Page<ServiceProvidedDto>> findAllByPetEstablishmentId(Long id, int index, int itemsPerPage) {

        log.info("findAll() - buscando serviços prestados por um estabelecimento, na página {} com {} itens por página.", index, itemsPerPage);

        Page<ServiceProvided> servicesProvided = serviceProvidedService.findAllByPetEstablishmentId(id, index, itemsPerPage);

        return ResponseEntity.ok(servicesProvided.map(DTOConverterUtil::toDto));
    }

    /**
     * Cria um novo serviço prestado.
     *
     * @param entity Objeto {@link ServiceProvided} contendo os dados do novo serviço prestado.
     * @return {@link ResponseEntity} contendo o DTO do serviço prestado criado.
     */
    @Override
    public ResponseEntity<ServiceProvidedDto> create(ServiceProvided entity) {

        log.info("create() - recebendo solicitação para criar um novo serviço prestado.");

        return ResponseEntity.ok(toDto(serviceProvidedService.create(entity)));
    }

    /**
     * Cria uma lista de novos serviços prestados.
     *
     * @param servicesProvided Lista de objetos {@link ServiceProvided} contendo os dados dos serviços prestados.
     * @return {@link ResponseEntity} contendo uma lista de DTOs dos serviços prestados criados.
     */
    @Override
    public ResponseEntity<List<ServiceProvidedDto>> create(List<ServiceProvided> servicesProvided) {

        log.info("create() - recebendo solicitação para criar múltiplos serviços prestados.");

        return ResponseEntity.ok(serviceProvidedService.create(servicesProvided).stream().map(DTOConverterUtil::toDto).toList());
    }

    /**
     * Atualiza um serviço prestado existente com base no ID fornecido.
     *
     * @param id     ID do serviço prestado a ser atualizado.
     * @param entity Objeto {@link ServiceProvided} contendo os novos dados do serviço prestado.
     * @return {@link ResponseEntity} contendo o DTO do serviço prestado atualizado.
     */
    @Override
    public ResponseEntity<ServiceProvidedDto> update(Long id, ServiceProvided entity) {

        log.info("update() - recebendo solicitação para atualizar o serviço prestado com id: {}", id);

        return ResponseEntity.ok(toDto(serviceProvidedService.update(id, entity)));
    }

    /**
     * Deleta um serviço prestado existente com base no ID fornecido.
     *
     * @param id ID do serviço prestado a ser deletado.
     * @return {@link ResponseEntity} sem conteúdo com status HTTP 204 (No Content).
     */
    @Override
    public ResponseEntity<Void> delete(Long id) {

        log.info("delete() - recebendo solicitação para deletar o serviço prestado com id: {}", id);

        serviceProvidedService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
