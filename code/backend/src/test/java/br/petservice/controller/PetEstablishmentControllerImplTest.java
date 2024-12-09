package br.petservice.controller;

import br.petservice.backend.controller.PetEstablishmentControllerImpl;
import br.petservice.backend.model.PetEstablishment;
import br.petservice.backend.model.abstracts.LegalEntity;
import br.petservice.backend.model.dto.PetEstablishmentDto;
import br.petservice.backend.security.JwtService;
import br.petservice.backend.service.interfaces.UserService;
import br.petservice.backend.util.DTOConverterUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetEstablishmentControllerImplTest {

    @Mock
    private UserService<PetEstablishment> userService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private PetEstablishmentControllerImpl petEstablishmentController;

    @Test
    void testFindMe() {
        PetEstablishment petEstablishment = new PetEstablishment();
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(petEstablishment);

        ResponseEntity<PetEstablishmentDto> response = petEstablishmentController.findMe();

        assertEquals(DTOConverterUtil.toDto((LegalEntity) petEstablishment), response.getBody());
    }

    @Test
    void testFindById() {
        Long id = 1L;
        PetEstablishment petEstablishment = new PetEstablishment();
        when(userService.findById(id)).thenReturn(petEstablishment);

        ResponseEntity<PetEstablishmentDto> response = petEstablishmentController.findById(id);

        assertEquals(DTOConverterUtil.toDto(petEstablishment), response.getBody());
    }

    @Test
    void testFindAll() {
        int index = 0;
        int itemsPerPage = 10;
        Page<PetEstablishment> page = Page.empty();
        when(userService.findAll(index, itemsPerPage)).thenReturn(page);

        ResponseEntity<Page<PetEstablishmentDto>> response = petEstablishmentController.findAll(index, itemsPerPage);

        assertEquals(page.map(DTOConverterUtil::toDto), response.getBody());
    }

    @Test
    void testCreate() {
        PetEstablishment petEstablishment = new PetEstablishment();
        when(userService.create(petEstablishment)).thenReturn(petEstablishment);

        ResponseEntity<PetEstablishmentDto> response = petEstablishmentController.create(petEstablishment);

        assertEquals(DTOConverterUtil.toDto(petEstablishment), response.getBody());
    }

    @Test
    void testUpdate() {
        Long id = 1L;
        PetEstablishment petEstablishment = new PetEstablishment();
        when(userService.update(id, petEstablishment)).thenReturn(petEstablishment);

        ResponseEntity<PetEstablishmentDto> response = petEstablishmentController.update(id, petEstablishment);

        assertEquals(DTOConverterUtil.toDto(petEstablishment), response.getBody());
    }

    @Test
    void testDelete() {
        Long id = 1L;

        ResponseEntity<Void> response = petEstablishmentController.delete(id);

        assertEquals(204, response.getStatusCodeValue());
    }
}