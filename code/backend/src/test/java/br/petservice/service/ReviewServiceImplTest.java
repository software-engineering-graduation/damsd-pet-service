package br.petservice.service;

import br.petservice.backend.model.Review;
import br.petservice.backend.repository.ReviewRepository;
import br.petservice.backend.service.ReviewServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static br.petservice.backend.enums.ExceptionMessagesEnum.MSG_REVIEW_FOUND_EXCEPTION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    void testFindById() {
        Review review = new Review();
        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.of(review));

        Review result = reviewService.findById(1L);

        assertNotNull(result);
        verify(reviewRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            reviewService.findById(1L);
        });

        assertEquals(MSG_REVIEW_FOUND_EXCEPTION.getMessage(), exception.getMessage());
    }

    @Test
    void testFindAll() {
        Page<Review> page = new PageImpl<>(List.of(new Review()));
        when(reviewRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Review> result = reviewService.findAll(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(reviewRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testCreate() {
        Review review = new Review();
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Review result = reviewService.create(review);

        assertNotNull(result);
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void testUpdate() {
        Review review = new Review();
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Review result = reviewService.update(1L, review);

        assertNotNull(result);
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void testDelete() {
        Review review = new Review();
        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.of(review));

        reviewService.delete(1L);

        verify(reviewRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindAllByAuthorId() {
        Page<Review> page = new PageImpl<>(List.of(new Review()));
        when(reviewRepository.findAllByAuthorId(any(Long.class), any(PageRequest.class))).thenReturn(page);

        Page<Review> result = reviewService.findAllByAuthorId(1L, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(reviewRepository, times(1)).findAllByAuthorId(any(Long.class), any(PageRequest.class));
    }
}