package br.petservice.controller;

import br.petservice.backend.controller.PetControllerImpl;
import br.petservice.backend.model.Pet;
import br.petservice.backend.model.dto.PetDto;
import br.petservice.backend.service.interfaces.PetService;
import br.petservice.backend.util.DTOConverterUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetControllerImplTest {

    @Mock
    private PetService petService;

    @InjectMocks
    private PetControllerImpl petController;

    @Test
    void testFindById() {
        Long id = 1L;
        Pet pet = new Pet();
        when(petService.findById(id)).thenReturn(pet);

        ResponseEntity<PetDto> response = petController.findById(id);

        assertEquals(DTOConverterUtil.toDto(pet), response.getBody());
    }

    @Test
    void testFindAll() {
        int index = 0;
        int itemsPerPage = 10;
        Page<Pet> page = Page.empty();
        when(petService.findAll(index, itemsPerPage)).thenReturn(page);

        ResponseEntity<Page<PetDto>> response = petController.findAll(index, itemsPerPage);

        assertEquals(page.map(DTOConverterUtil::toDto), response.getBody());
    }

    @Test
    void testFindAllByPetGuardianId() {
        Long id = 1L;
        int index = 0;
        int itemsPerPage = 10;
        Page<Pet> page = Page.empty();
        when(petService.findAllByPetGuardianId(id, index, itemsPerPage)).thenReturn(page);

        ResponseEntity<Page<PetDto>> response = petController.findAllByPetGuardianId(id, index, itemsPerPage);

        assertEquals(page.map(DTOConverterUtil::toDto), response.getBody());
    }

    @Test
    void testCreate() {
        Pet pet = new Pet();
        when(petService.create(pet)).thenReturn(pet);

        ResponseEntity<PetDto> response = petController.create(pet);

        assertEquals(DTOConverterUtil.toDto(pet), response.getBody());
    }

    @Test
    void testUpdate() {
        Long id = 1L;
        Pet pet = new Pet();
        when(petService.update(id, pet)).thenReturn(pet);

        ResponseEntity<PetDto> response = petController.update(id, pet);

        assertEquals(DTOConverterUtil.toDto(pet), response.getBody());
    }

    @Test
    void testDelete() {
        Long id = 1L;

        ResponseEntity<Void> response = petController.delete(id);

        assertEquals(204, response.getStatusCodeValue());
    }
}