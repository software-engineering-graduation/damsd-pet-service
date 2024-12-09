package br.petservice.backend.controller.interfaces;

import br.petservice.backend.model.Pet;
import br.petservice.backend.model.dto.PetDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Interface responsável por definir as operações específicas do controlador de pets.
 * Faz extend da interface {@link CrudController} para herdar as operações CRUD básicas.
 */
public interface PetController extends CrudController<Pet, PetDto> {

    @GetMapping("/pet-guardian/{id}/{index}/{itemsPerPage}")
    ResponseEntity<Page<PetDto>> findAllByPetGuardianId(@PathVariable Long id,
                                                        @PathVariable int index,
                                                        @PathVariable int itemsPerPage);
}
