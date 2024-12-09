package br.petservice.controller;

import br.petservice.backend.controller.AddressControllerImpl;
import br.petservice.backend.service.interfaces.AddressService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressControllerImplTest {

    @Mock
    private AddressService addressService;

    @InjectMocks
    private AddressControllerImpl addressController;

    @Test
    void testGetCepInfo() {
        String cep = "12345678";
        Map<String, Object> expectedResponse = Map.of("key", "value");
        when(addressService.getCepInfo(cep)).thenReturn(expectedResponse);

        ResponseEntity<Map<String, Object>> response = addressController.getCepInfo(cep);

        assertEquals(expectedResponse, response.getBody());
    }
}