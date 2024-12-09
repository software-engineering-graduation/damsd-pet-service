package br.petservice.service;

import br.petservice.backend.model.CategoryTag;
import br.petservice.backend.repository.CategoryTagRepository;
import br.petservice.backend.service.CategoryTagServiceImpl;
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

import static br.petservice.backend.enums.ExceptionMessagesEnum.MSG_CATEGORY_TAG_FOUND_EXCEPTION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryTagServiceImplTest {

    @Mock
    private CategoryTagRepository categoryTagRepository;

    @InjectMocks
    private CategoryTagServiceImpl categoryTagService;

    @Test
    void testFindById() {
        CategoryTag categoryTag = new CategoryTag();
        when(categoryTagRepository.findById(any(Long.class))).thenReturn(Optional.of(categoryTag));

        CategoryTag result = categoryTagService.findById(1L);

        assertNotNull(result);
        verify(categoryTagRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(categoryTagRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            categoryTagService.findById(1L);
        });

        assertEquals(MSG_CATEGORY_TAG_FOUND_EXCEPTION.getMessage(), exception.getMessage());
    }

    @Test
    void testFindAll() {
        Page<CategoryTag> page = new PageImpl<>(List.of(new CategoryTag()));
        when(categoryTagRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<CategoryTag> result = categoryTagService.findAll(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(categoryTagRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testCreate() {
        CategoryTag categoryTag = new CategoryTag();
        when(categoryTagRepository.save(any(CategoryTag.class))).thenReturn(categoryTag);

        CategoryTag result = categoryTagService.create(categoryTag);

        assertNotNull(result);
        verify(categoryTagRepository, times(1)).save(categoryTag);
    }

    @Test
    void testUpdate() {
        CategoryTag categoryTag = new CategoryTag();
        when(categoryTagRepository.findById(any(Long.class))).thenReturn(Optional.of(categoryTag));
        when(categoryTagRepository.save(any(CategoryTag.class))).thenReturn(categoryTag);

        CategoryTag result = categoryTagService.update(1L, categoryTag);

        assertNotNull(result);
        verify(categoryTagRepository, times(1)).save(categoryTag);
    }

    @Test
    void testDelete() {
        CategoryTag categoryTag = new CategoryTag();
        when(categoryTagRepository.findById(any(Long.class))).thenReturn(Optional.of(categoryTag));

        categoryTagService.delete(1L);

        verify(categoryTagRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByCategoryTagNameIn() {
        List<CategoryTag> categoryTags = List.of(new CategoryTag());
        when(categoryTagRepository.findByCategoryNameIn(any(List.class))).thenReturn(categoryTags);

        List<CategoryTag> result = categoryTagService.findByCategoryTagNameIn(List.of("tag"));

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(categoryTagRepository, times(1)).findByCategoryNameIn(any(List.class));
    }
}