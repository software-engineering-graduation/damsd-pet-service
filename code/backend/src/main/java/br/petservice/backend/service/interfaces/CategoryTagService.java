package br.petservice.backend.service.interfaces;

import br.petservice.backend.model.CategoryTag;

import java.util.List;

public interface CategoryTagService extends CrudService<CategoryTag> {

    List<CategoryTag> findByCategoryTagNameIn(List<String> categories);
}
