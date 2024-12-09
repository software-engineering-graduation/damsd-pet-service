package br.petservice.backend.service.interfaces;

import org.springframework.data.domain.Page;

/**
 * Interface de métodos gerais da camada de serviço
 *
 * @param <T> classe de referência
 */
public interface CrudService<T> {

    T findById(Long id);

    Page<T> findAll(int index, int itemsPerPage);

    T create(T entity);

    T update(Long id, T entity);

    void delete(Long id);
}
