package br.petservice.backend.service;

import br.petservice.backend.service.interfaces.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static br.petservice.backend.util.Constants.VIA_CEP_RETURN_TYPE;
import static br.petservice.backend.util.Constants.VIA_CEP_URL;
import static java.lang.String.format;
import static org.springframework.http.HttpMethod.GET;

/**
 * Implementação do serviço responsável por buscar informações de endereço a partir de um CEP.
 * Este serviço faz uma requisição HTTP para a API externa ViaCEP, retornando os dados do endereço
 * no formato de um Map<String, Object>.
 */
@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    /**
     * Faz uma chamada para a API ViaCEP e retorna as informações de endereço de um determinado CEP.
     *
     * @param cep o Código de Endereçamento Postal (CEP) que será utilizado para a busca do endereço
     * @return um Map contendo as informações do endereço retornadas pela API ViaCEP
     */
    @Override
    @Cacheable("cep")
    public Map<String, Object> getCepInfo(String cep) {

        RestTemplate restTemplate = new RestTemplate();
        String url = format(VIA_CEP_URL + cep + VIA_CEP_RETURN_TYPE);

        return restTemplate.exchange(url, GET, null, new ParameterizedTypeReference<Map<String, Object>>() {
        }).getBody();
    }
}
