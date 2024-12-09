package br.petservice.service;

import br.petservice.backend.model.PetEstablishment;
import br.petservice.backend.model.Review;
import br.petservice.backend.model.dto.LoginUserDto;
import br.petservice.backend.repository.UserRepository;
import br.petservice.backend.service.PetEstablishmentServiceImpl;
import br.petservice.backend.service.interfaces.AuthenticationService;
import br.petservice.backend.service.interfaces.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
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
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetEstablishmentServiceImplTest {

    @Mock
    private UserRepository<PetEstablishment> userRepository;

    @Mock
    private AuthenticationService<PetEstablishment> authenticationService;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private PetEstablishmentServiceImpl petEstablishmentService;

    @Test
    void testRegister() {
        PetEstablishment petEstablishment = new PetEstablishment();
        when(authenticationService.register(any(PetEstablishment.class))).thenReturn(petEstablishment);

        PetEstablishment result = petEstablishmentService.register(petEstablishment);

        assertNotNull(result);
        verify(authenticationService, times(1)).register(petEstablishment);
    }

    @Test
    void testAuthenticate() {
        LoginUserDto loginUserDto = new LoginUserDto("email", "password");
        PetEstablishment petEstablishment = new PetEstablishment();
        when(authenticationService.authenticate(any(LoginUserDto.class))).thenReturn(petEstablishment);

        PetEstablishment result = petEstablishmentService.authenticate(loginUserDto);

        assertNotNull(result);
        verify(authenticationService, times(1)).authenticate(loginUserDto);
    }

    @Test
    void testFindById() {
        PetEstablishment petEstablishment = new PetEstablishment();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(petEstablishment));

        PetEstablishment result = petEstablishmentService.findById(1L);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            petEstablishmentService.findById(1L);
        });

        assertEquals(MSG_USER_NOT_FOUND_EXCEPTION.getMessage(), exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAll() {
        Page<PetEstablishment> page = new PageImpl<>(singletonList(new PetEstablishment()));
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<PetEstablishment> result = petEstablishmentService.findAll(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(userRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testFindAllWithRatings() {
        PetEstablishment petEstablishment = new PetEstablishment();
        petEstablishment.setId(1L);

        Review review = new Review();
        review.setReviewedUserId(1L);
        review.setRating(5);

        Review review2 = new Review();
        review2.setReviewedUserId(1L);
        review2.setRating(4);

        Page<PetEstablishment> page = new PageImpl<>(singletonList(petEstablishment));
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(page);
        when(reviewService.findAllByReviewedUserIds(singletonList(1L))).thenReturn(List.of(review, review2));

        Page<PetEstablishment> result = petEstablishmentService.findAll(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(4.5, result.getContent().get(0).getAverageRating());

        verify(userRepository).findAll(any(PageRequest.class));
        verify(reviewService).findAllByReviewedUserIds(any());
    }
    @Test
    void testCreate() {
        PetEstablishment petEstablishment = new PetEstablishment();
        when(authenticationService.register(any(PetEstablishment.class))).thenReturn(petEstablishment);

        PetEstablishment result = petEstablishmentService.create(petEstablishment);

        assertNotNull(result);
        verify(authenticationService, times(1)).register(petEstablishment);
    }

    @Test
    void testUpdate() {
        PetEstablishment existingPetEstablishment = new PetEstablishment();
        PetEstablishment updatedPetEstablishment = new PetEstablishment();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingPetEstablishment));
        when(userRepository.save(any(PetEstablishment.class))).thenReturn(updatedPetEstablishment);

        PetEstablishment result = petEstablishmentService.update(1L, updatedPetEstablishment);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingPetEstablishment);
    }

    @Test
    void testDelete() {
        PetEstablishment petEstablishment = new PetEstablishment();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(petEstablishment));

        petEstablishmentService.delete(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}