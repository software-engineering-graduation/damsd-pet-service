package br.petservice.service;

import br.petservice.backend.service.AddressServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static br.petservice.backend.util.Constants.VIA_CEP_RETURN_TYPE;
import static br.petservice.backend.util.Constants.VIA_CEP_URL;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @InjectMocks
    private AddressServiceImpl addressService;

@Test
void testGetCepInfo() {
    String cep = "01001000";
    String url = format(VIA_CEP_URL + cep + VIA_CEP_RETURN_TYPE);
    RestTemplate restTemplate = mock(RestTemplate.class);
    Map<String, Object> expectedResponse = new HashMap<>();
    expectedResponse.put("cep", "01001-000");
    expectedResponse.put("logradouro", "Praça da Sé");
    expectedResponse.put("complemento", "lado ímpar");
    expectedResponse.put("unidade", "");
    expectedResponse.put("bairro", "Sé");
    expectedResponse.put("localidade", "São Paulo");
    expectedResponse.put("uf", "SP");
    expectedResponse.put("estado", "São Paulo");
    expectedResponse.put("regiao", "Sudeste");
    expectedResponse.put("ibge", "3550308");
    expectedResponse.put("gia", "1004");
    expectedResponse.put("ddd", "11");
    expectedResponse.put("siafi", "7107");

    Map<String, Object> actualResponse = addressService.getCepInfo(cep);

    assertEquals(expectedResponse, actualResponse);
}
}