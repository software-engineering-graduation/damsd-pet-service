package br.petservice.backend.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * Interface de endpoints de controle de endereço (API Via Cep)
 */
public interface AddressController {

    @GetMapping("/{cep}")
    ResponseEntity<Map<String, Object>> getCepInfo(@PathVariable String cep);
}
