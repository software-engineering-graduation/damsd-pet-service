package br.petservice.backend.controller;

import br.petservice.backend.controller.interfaces.AddressController;
import br.petservice.backend.service.interfaces.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controlador responsável por gerenciar operações relacionadas a endereços.
 * Essa implementação utiliza um serviço de endereços para buscar informações de CEPs.
 */
@Slf4j
@RestController
@Validated
@RequestMapping(value = "/address")
@AllArgsConstructor
@Tag(name = "AddressController", description = "API para operações de cep.")
public class AddressControllerImpl implements AddressController {

    private final AddressService addressService;

    /**
     * Busca as informações relacionadas a um determinado CEP.
     *
     * @param cep o código postal (CEP) que será utilizado para busca
     * @return ResponseEntity contendo um Map com as informações do endereço obtidas a partir do CEP
     */
    @Override
    @Operation(summary = "Buscar informações de endereço pelo CEP",
            description = "Retorna um mapa com as informações do endereço obtidas a partir do CEP fornecido.")
    public ResponseEntity<Map<String, Object>> getCepInfo(@PathVariable String cep) {

        log.info("getCepInfo() - buscando informações para o CEP: {}", cep);

        return ResponseEntity.ok(addressService.getCepInfo(cep));
    }
}
