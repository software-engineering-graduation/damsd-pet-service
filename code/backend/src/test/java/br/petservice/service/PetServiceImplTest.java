package br.petservice.service;

import br.petservice.backend.model.Pet;
import br.petservice.backend.model.PetGuardian;
import br.petservice.backend.repository.PetRepository;
import br.petservice.backend.service.PetServiceImpl;
import br.petservice.backend.util.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static br.petservice.backend.enums.ExceptionMessagesEnum.MSG_PET_NOT_FOUND_EXCEPTION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetServiceImplTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetServiceImpl petService;

    @BeforeAll
    public static void setUp() {

        PetGuardian petGuardian = new PetGuardian();
        petGuardian.setId(1L);
        when(SecurityUtil.getAuthenticatedPetGuardian()).thenReturn(petGuardian);
    }

    @Test
    public void testFindById() {
        Pet pet = new Pet();
        when(petRepository.findById(anyLong())).thenReturn(Optional.of(pet));

        Pet result = petService.findById(1L);

        assertNotNull(result);
        verify(petRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindByIdNotFound() {
        when(petRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            petService.findById(1L);
        });

        assertEquals(MSG_PET_NOT_FOUND_EXCEPTION.getMessage(), exception.getMessage());
        verify(petRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindAll() {
        Page<Pet> page = new PageImpl<>(Collections.singletonList(new Pet()));
        when(petRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Pet> result = petService.findAll(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(petRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    public void testFindAllByPetGuardianId() {
        Page<Pet> page = new PageImpl<>(Collections.singletonList(new Pet()));
        when(petRepository.findAllByPetGuardianId(anyLong(), any(PageRequest.class))).thenReturn(Optional.of(page));

        Page<Pet> result = petService.findAllByPetGuardianId(1L, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(petRepository, times(1)).findAllByPetGuardianId(1L, PageRequest.of(0, 10));
    }

    @Test
    public void testPetGuardianHasPet() {
        Pet pet = new Pet();
        PetGuardian petGuardian = new PetGuardian();
        petGuardian.setId(1L);
        pet.setPetGuardian(petGuardian);
        when(petRepository.findById(anyLong())).thenReturn(Optional.of(pet));

        boolean result = petService.petGuardianHasPet(1L);

        assertTrue(result);
        verify(petRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreate() {
        Pet pet = new Pet();
        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        Pet result = petService.create(pet);

        assertNotNull(result);
        verify(petRepository, times(1)).save(pet);
    }

    @Test
    public void testUpdate() {
        Pet existingPet = new Pet();
        Pet updatedPet = new Pet();
        when(petRepository.findById(anyLong())).thenReturn(Optional.of(existingPet));
        when(petRepository.save(any(Pet.class))).thenReturn(updatedPet);

        Pet result = petService.update(1L, updatedPet);

        assertNotNull(result);
        verify(petRepository, times(1)).findById(1L);
        verify(petRepository, times(1)).save(existingPet);
    }

    @Test
    public void testDelete() {
        Pet pet = new Pet();
        when(petRepository.findById(anyLong())).thenReturn(Optional.of(pet));

        petService.delete(1L);

        verify(petRepository, times(1)).findById(1L);
        verify(petRepository, times(1)).deleteById(1L);
    }
}