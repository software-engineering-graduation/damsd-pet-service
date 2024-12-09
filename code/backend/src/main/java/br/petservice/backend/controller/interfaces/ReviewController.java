package br.petservice.backend.controller.interfaces;

import br.petservice.backend.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ReviewController extends CrudController<Review, Review> {

    @GetMapping("/{id}/{index}/{itemsPerPage}")
    ResponseEntity<Page<Review>> findAllByAuthorId(@PathVariable Long id,
                                                   @PathVariable int index,
                                                   @PathVariable int itemsPerPage);
}
