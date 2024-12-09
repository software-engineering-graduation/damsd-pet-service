package br.petservice.controller;

import br.petservice.backend.controller.ReviewControllerImpl;
import br.petservice.backend.model.Review;
import br.petservice.backend.service.interfaces.ReviewService;
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
class ReviewControllerImplTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewControllerImpl reviewController;

    @Test
    void testFindById() {
        Long id = 1L;
        Review review = new Review();
        when(reviewService.findById(id)).thenReturn(review);

        ResponseEntity<Review> response = reviewController.findById(id);

        assertEquals(review, response.getBody());
    }

    @Test
    void testFindAll() {
        int index = 0;
        int itemsPerPage = 10;
        Page<Review> page = Page.empty();
        when(reviewService.findAll(index, itemsPerPage)).thenReturn(page);

        ResponseEntity<Page<Review>> response = reviewController.findAll(index, itemsPerPage);

        assertEquals(page, response.getBody());
    }

    @Test
    void testCreate() {
        Review review = new Review();
        when(reviewService.create(review)).thenReturn(review);

        ResponseEntity<Review> response = reviewController.create(review);

        assertEquals(review, response.getBody());
    }

    @Test
    void testUpdate() {
        Long id = 1L;
        Review review = new Review();
        when(reviewService.update(id, review)).thenReturn(review);

        ResponseEntity<Review> response = reviewController.update(id, review);

        assertEquals(review, response.getBody());
    }

    @Test
    void testDelete() {
        Long id = 1L;

        ResponseEntity<Void> response = reviewController.delete(id);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testFindAllByAuthorId() {
        Long id = 1L;
        int index = 0;
        int itemsPerPage = 10;
        Page<Review> page = Page.empty();
        when(reviewService.findAllByAuthorId(id, index, itemsPerPage)).thenReturn(page);

        ResponseEntity<Page<Review>> response = reviewController.findAllByAuthorId(id, index, itemsPerPage);

        assertEquals(page, response.getBody());
    }
}