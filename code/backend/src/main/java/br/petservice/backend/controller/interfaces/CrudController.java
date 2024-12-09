package br.petservice.backend.controller.interfaces;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Interface de métodos gerais da camada de controle
 *
 * @param <I> classe de entrada (input)
 * @param <O> classe de saída (output)
 */
public interface CrudController<I, O> {

    @GetMapping("/{id}")
    ResponseEntity<O> findById(@PathVariable Long id);

    @GetMapping("/{index}/{itemsPerPage}")
    ResponseEntity<Page<O>> findAll(@PathVariable int index, @PathVariable int itemsPerPage);

    @PostMapping
    ResponseEntity<O> create(@Valid @RequestBody I entity);

    @PutMapping("/{id}")
    ResponseEntity<O> update(@PathVariable Long id, @Valid @RequestBody I entity);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}
