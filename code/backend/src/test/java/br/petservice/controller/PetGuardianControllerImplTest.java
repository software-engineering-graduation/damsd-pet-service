package br.petservice.controller;

import br.petservice.backend.controller.PetGuardianControllerImpl;
import br.petservice.backend.model.PetGuardian;
import br.petservice.backend.model.dto.PetGuardianDto;
import br.petservice.backend.security.JwtService;
import br.petservice.backend.service.interfaces.UserService;
import br.petservice.backend.util.DTOConverterUtil;
import br.petservice.backend.util.SecurityUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import static br.petservice.backend.util.SecurityUtil.getAuthenticatedPetGuardian;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetGuardianControllerImplTest {

    @Mock
    private UserService<PetGuardian> userService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private PetGuardianControllerImpl petGuardianController;

    @Test
    void testFindById() {
        Long id = 1L;
        PetGuardian petGuardian = new PetGuardian();
        when(userService.findById(id)).thenReturn(petGuardian);

        ResponseEntity<PetGuardianDto> response = petGuardianController.findById(id);

        assertEquals(DTOConverterUtil.toDto(petGuardian), response.getBody());
    }

    @Test
    void testFindAll() {
        int index = 0;
        int itemsPerPage = 10;
        Page<PetGuardian> page = Page.empty();
        when(userService.findAll(index, itemsPerPage)).thenReturn(page);

        ResponseEntity<Page<PetGuardianDto>> response = petGuardianController.findAll(index, itemsPerPage);

        assertEquals(page.map(DTOConverterUtil::toDto), response.getBody());
    }

    @Test
    void testCreate() {
        PetGuardian petGuardian = new PetGuardian();
        when(userService.create(petGuardian)).thenReturn(petGuardian);

        ResponseEntity<PetGuardianDto> response = petGuardianController.create(petGuardian);

        assertEquals(DTOConverterUtil.toDto(petGuardian), response.getBody());
    }

    @Test
    void testUpdate() {
        Long id = 1L;
        PetGuardian petGuardian = new PetGuardian();
        when(userService.update(id, petGuardian)).thenReturn(petGuardian);

        ResponseEntity<PetGuardianDto> response = petGuardianController.update(id, petGuardian);

        assertEquals(DTOConverterUtil.toDto(petGuardian), response.getBody());
    }

    @Test
    void testDelete() {
        Long id = 1L;

        ResponseEntity<Void> response = petGuardianController.delete(id);

        assertEquals(204, response.getStatusCodeValue());
    }
}