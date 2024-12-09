package br.petservice.service;

import static br.petservice.backend.enums.ExceptionMessagesEnum.MSG_SERVICE_PROVIDED_NOT_FOUND_EXCEPTION;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.petservice.backend.model.CategoryTag;
import br.petservice.backend.model.PetEstablishment;
import br.petservice.backend.model.Review;
import br.petservice.backend.model.ServiceProvided;
import br.petservice.backend.repository.ServiceProvidedRepository;
import br.petservice.backend.service.ServiceProvidedServiceImpl;
import br.petservice.backend.service.interfaces.CategoryTagService;
import br.petservice.backend.service.interfaces.ReviewService;
import br.petservice.backend.service.interfaces.UserService;
import br.petservice.backend.util.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
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

@ExtendWith(MockitoExtension.class)
class ServiceProvidedServiceImplTest {

    @Mock private CategoryTagService categoryTagService;

    @Mock private UserService<PetEstablishment> petEstablishmentService;

    @Mock private ServiceProvidedRepository serviceProvidedRepository;

    @Mock private ReviewService reviewService;

    @InjectMocks private ServiceProvidedServiceImpl serviceProvidedService;

    @BeforeAll
    static void setUp() {
        Mockito.mockStatic(SecurityUtil.class);

        PetEstablishment petEstablishment = new PetEstablishment();
        petEstablishment.setId(1L);
        when(SecurityUtil.getAuthenticatedPetEstablishment()).thenReturn(petEstablishment);
    }

    @Test
    void testFindById() {
        ServiceProvided serviceProvided = new ServiceProvided();
        when(serviceProvidedRepository.findById(anyLong())).thenReturn(Optional.of(serviceProvided));

        ServiceProvided result = serviceProvidedService.findById(1L);

        assertNotNull(result);
        verify(serviceProvidedRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(serviceProvidedRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> {
                            serviceProvidedService.findById(1L);
                        });

        assertEquals(MSG_SERVICE_PROVIDED_NOT_FOUND_EXCEPTION.getMessage(), exception.getMessage());
        verify(serviceProvidedRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAll() {
        Page<ServiceProvided> page = new PageImpl<>(singletonList(new ServiceProvided()));
        when(serviceProvidedRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<ServiceProvided> result = serviceProvidedService.findAll(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(serviceProvidedRepository, times(1)).findAll(any(PageRequest.class));
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

        ServiceProvided serviceProvided = new ServiceProvided();
        serviceProvided.setPetEstablishment(petEstablishment);

        Page<ServiceProvided> page = new PageImpl<>(singletonList(serviceProvided));
        when(serviceProvidedRepository.findAll( any(PageRequest.class)))
                .thenReturn(page);
        when(reviewService.findAllByReviewedUserIds(singletonList(1L)))
                .thenReturn(List.of(review, review2));

        Page<ServiceProvided> result = serviceProvidedService.findAll( 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(4.5, result.getContent().get(0).getPetEstablishment().getAverageRating());
        verify(serviceProvidedRepository).findAll( PageRequest.of(0, 10));
        verify(reviewService).findAllByReviewedUserIds(anyList());
    }

    @Test
    void testCreate() {
        ServiceProvided serviceProvided = new ServiceProvided();
        serviceProvided.setCategoryTag(new CategoryTag());
        PetEstablishment petEstablishment = new PetEstablishment();
        CategoryTag categoryTag = new CategoryTag();
        when(petEstablishmentService.findById(anyLong())).thenReturn(petEstablishment);
        when(categoryTagService.findByCategoryTagNameIn(anyList()))
                .thenReturn(singletonList(categoryTag));
        when(serviceProvidedRepository.saveAll(anyList())).thenReturn(singletonList(serviceProvided));

        ServiceProvided result = serviceProvidedService.create(serviceProvided);

        assertNotNull(result);
        verify(serviceProvidedRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testUpdate() {
        ServiceProvided existingServiceProvided = new ServiceProvided();
        existingServiceProvided.setCategoryTag(new CategoryTag());
        ServiceProvided updatedServiceProvided = new ServiceProvided();
        updatedServiceProvided.setCategoryTag(new CategoryTag());
        CategoryTag categoryTag = new CategoryTag();
        when(serviceProvidedRepository.findById(anyLong()))
                .thenReturn(Optional.of(existingServiceProvided));
        when(categoryTagService.findByCategoryTagNameIn(anyList()))
                .thenReturn(singletonList(categoryTag));
        when(serviceProvidedRepository.save(any(ServiceProvided.class)))
                .thenReturn(updatedServiceProvided);

        ServiceProvided result = serviceProvidedService.update(1L, updatedServiceProvided);

        assertNotNull(result);
        verify(serviceProvidedRepository, times(1)).findById(1L);
        verify(serviceProvidedRepository, times(1)).save(existingServiceProvided);
    }

    @Test
    void testDelete() {
        ServiceProvided serviceProvided = new ServiceProvided();
        when(serviceProvidedRepository.findById(anyLong())).thenReturn(Optional.of(serviceProvided));

        serviceProvidedService.delete(1L);

        verify(serviceProvidedRepository, times(1)).findById(1L);
        verify(serviceProvidedRepository, times(1)).deleteById(1L);
    }

    @Test
    void testPetEstablishmentHasServiceProvided() {
        ServiceProvided serviceProvided = new ServiceProvided();
        PetEstablishment petEstablishment = new PetEstablishment();
        petEstablishment.setId(1L);
        serviceProvided.setPetEstablishment(petEstablishment);
        when(serviceProvidedRepository.findById(anyLong())).thenReturn(Optional.of(serviceProvided));

        boolean result = serviceProvidedService.petEstablishmentHasServiceProvided(1L);

        assertTrue(result);
        verify(serviceProvidedRepository, times(1)).findById(1L);
    }
}
