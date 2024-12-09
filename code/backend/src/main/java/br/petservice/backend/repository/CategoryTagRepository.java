package br.petservice.backend.repository;

import br.petservice.backend.model.CategoryTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryTagRepository extends JpaRepository<CategoryTag, Long> {

    List<CategoryTag> findByCategoryNameIn(List<String> categoryTagNames);
}
