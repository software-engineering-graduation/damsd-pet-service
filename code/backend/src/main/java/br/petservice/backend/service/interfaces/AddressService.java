package br.petservice.backend.service.interfaces;

import java.util.Map;

/**
 * Interface de métodos de endereço (API Via Cep)
 */
public interface AddressService {

    Map<String, Object> getCepInfo(String cep);
}
