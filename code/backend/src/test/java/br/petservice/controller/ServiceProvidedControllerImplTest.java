package br.petservice.controller;

import br.petservice.backend.controller.ServiceProvidedControllerImpl;
import br.petservice.backend.model.CategoryTag;
import br.petservice.backend.model.ServiceProvided;
import br.petservice.backend.model.dto.ServiceProvidedDto;
import br.petservice.backend.service.interfaces.ServiceProvidedService;
import br.petservice.backend.util.DTOConverterUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static br.petservice.backend.util.DTOConverterUtil.toDto;

@ExtendWith(MockitoExtension.class)
class ServiceProvidedControllerImplTest {

    @Mock
    private ServiceProvidedService serviceProvidedService;

    @InjectMocks
    private ServiceProvidedControllerImpl serviceProvidedController;

    @Test
    void testFindById() {
        Long id = 1L;
        ServiceProvided serviceProvided = new ServiceProvided();
        serviceProvided.setCategoryTag(new CategoryTag());
        when(serviceProvidedService.findById(id)).thenReturn(serviceProvided);

        ResponseEntity<ServiceProvidedDto> response = serviceProvidedController.findById(id);

        assertEquals(toDto(serviceProvided), response.getBody());
    }

    @Test
    void testFindAll() {
        int index = 0;
        int itemsPerPage = 10;
        Page<ServiceProvided> page = Page.empty();
        when(serviceProvidedService.findAll(index, itemsPerPage)).thenReturn(page);

        ResponseEntity<Page<ServiceProvidedDto>> response = serviceProvidedController.findAll(index, itemsPerPage);

        assertEquals(page.map(DTOConverterUtil::toDto), response.getBody());
    }

    @Test
    void testFindAllByPetEstablishmentId() {
        Long id = 1L;
        int index = 0;
        int itemsPerPage = 10;
        Page<ServiceProvided> page = Page.empty();
        when(serviceProvidedService.findAllByPetEstablishmentId(id, index, itemsPerPage)).thenReturn(page);

        ResponseEntity<Page<ServiceProvidedDto>> response = serviceProvidedController.findAllByPetEstablishmentId(id, index, itemsPerPage);

        assertEquals(page.map(DTOConverterUtil::toDto), response.getBody());
    }

    @Test
    void testCreate() {
        ServiceProvided serviceProvided = new ServiceProvided();
        serviceProvided.setCategoryTag(new CategoryTag());
        when(serviceProvidedService.create(serviceProvided)).thenReturn(serviceProvided);

        ResponseEntity<ServiceProvidedDto> response = serviceProvidedController.create(serviceProvided);

        assertEquals(toDto(serviceProvided), response.getBody());
    }

    @Test
    void testCreateMultiple() {
        List<ServiceProvided> servicesProvided = List.of(new ServiceProvided());
        servicesProvided.forEach(serviceProvided -> serviceProvided.setCategoryTag(new CategoryTag()));
        when(serviceProvidedService.create(servicesProvided)).thenReturn(servicesProvided);

        ResponseEntity<List<ServiceProvidedDto>> response = serviceProvidedController.create(servicesProvided);

        assertEquals(servicesProvided.stream().map(DTOConverterUtil::toDto).toList(), response.getBody());
    }

    @Test
    void testUpdate() {
        Long id = 1L;
        ServiceProvided serviceProvided = new ServiceProvided();
        serviceProvided.setCategoryTag(new CategoryTag());
        when(serviceProvidedService.update(id, serviceProvided)).thenReturn(serviceProvided);

        ResponseEntity<ServiceProvidedDto> response = serviceProvidedController.update(id, serviceProvided);

        assertEquals(toDto(serviceProvided), response.getBody());
    }

    @Test
    void testDelete() {
        Long id = 1L;

        ResponseEntity<Void> response = serviceProvidedController.delete(id);

        assertEquals(204, response.getStatusCodeValue());
    }
}