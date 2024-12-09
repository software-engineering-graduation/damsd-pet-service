package br.petservice.controller;

import br.petservice.backend.controller.CategoryTagControllerImpl;
import br.petservice.backend.model.CategoryTag;
import br.petservice.backend.service.interfaces.CategoryTagService;
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
class CategoryTagControllerImplTest {

    @Mock
    private CategoryTagService categoryTagService;

    @InjectMocks
    private CategoryTagControllerImpl categoryTagController;

    @Test
    void testFindById() {
        Long id = 1L;
        CategoryTag categoryTag = new CategoryTag();
        when(categoryTagService.findById(id)).thenReturn(categoryTag);

        ResponseEntity<CategoryTag> response = categoryTagController.findById(id);

        assertEquals(categoryTag, response.getBody());
    }

    @Test
    void testFindAll() {
        int index = 0;
        int itemsPerPage = 10;
        Page<CategoryTag> page = Page.empty();
        when(categoryTagService.findAll(index, itemsPerPage)).thenReturn(page);

        ResponseEntity<Page<CategoryTag>> response = categoryTagController.findAll(index, itemsPerPage);

        assertEquals(page, response.getBody());
    }

    @Test
    void testCreate() {
        CategoryTag categoryTag = new CategoryTag();
        when(categoryTagService.create(categoryTag)).thenReturn(categoryTag);

        ResponseEntity<CategoryTag> response = categoryTagController.create(categoryTag);

        assertEquals(categoryTag, response.getBody());
    }

    @Test
    void testUpdate() {
        Long id = 1L;
        CategoryTag categoryTag = new CategoryTag();
        when(categoryTagService.update(id, categoryTag)).thenReturn(categoryTag);

        ResponseEntity<CategoryTag> response = categoryTagController.update(id, categoryTag);

        assertEquals(categoryTag, response.getBody());
    }

    @Test
    void testDelete() {
        Long id = 1L;

        ResponseEntity<Void> response = categoryTagController.delete(id);

        assertEquals(204, response.getStatusCodeValue());
    }
}