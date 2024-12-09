package br.petservice.service;

import br.petservice.backend.model.PetGuardian;
import br.petservice.backend.model.dto.LoginUserDto;
import br.petservice.backend.repository.UserRepository;
import br.petservice.backend.service.PetGuardianServiceImpl;
import br.petservice.backend.service.interfaces.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static br.petservice.backend.enums.ExceptionMessagesEnum.MSG_USER_NOT_FOUND_EXCEPTION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetGuardianServiceImplTest {

    @Mock
    private UserRepository<PetGuardian> userRepository;

    @Mock
    private AuthenticationService<PetGuardian> authenticationService;

    @InjectMocks
    private PetGuardianServiceImpl petGuardianService;

    @Test
    void testRegister() {
        PetGuardian petGuardian = new PetGuardian();
        when(authenticationService.register(any(PetGuardian.class))).thenReturn(petGuardian);

        PetGuardian result = petGuardianService.register(petGuardian);

        assertNotNull(result);
        verify(authenticationService, times(1)).register(petGuardian);
    }

    @Test
    void testAuthenticate() {
        LoginUserDto loginUserDto = new LoginUserDto("email", "password");
        PetGuardian petGuardian = new PetGuardian();
        when(authenticationService.authenticate(any(LoginUserDto.class))).thenReturn(petGuardian);

        PetGuardian result = petGuardianService.authenticate(loginUserDto);

        assertNotNull(result);
        verify(authenticationService, times(1)).authenticate(loginUserDto);
    }

    @Test
    void testFindById() {
        PetGuardian petGuardian = new PetGuardian();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(petGuardian));

        PetGuardian result = petGuardianService.findById(1L);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            petGuardianService.findById(1L);
        });

        assertEquals(MSG_USER_NOT_FOUND_EXCEPTION.getMessage(), exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAll() {
        Page<PetGuardian> page = new PageImpl<>(Collections.singletonList(new PetGuardian()));
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<PetGuardian> result = petGuardianService.findAll(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(userRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testCreate() {
        PetGuardian petGuardian = new PetGuardian();
        when(authenticationService.register(any(PetGuardian.class))).thenReturn(petGuardian);

        PetGuardian result = petGuardianService.create(petGuardian);

        assertNotNull(result);
        verify(authenticationService, times(1)).register(petGuardian);
    }

    @Test
    void testUpdate() {
        PetGuardian existingPetGuardian = new PetGuardian();
        PetGuardian updatedPetGuardian = new PetGuardian();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingPetGuardian));
        when(userRepository.save(any(PetGuardian.class))).thenReturn(updatedPetGuardian);

        PetGuardian result = petGuardianService.update(1L, updatedPetGuardian);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingPetGuardian);
    }

    @Test
    void testDelete() {
        PetGuardian petGuardian = new PetGuardian();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(petGuardian));

        petGuardianService.delete(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}