package br.petservice.backend.service.interfaces;

import br.petservice.backend.model.Pet;
import org.springframework.data.domain.Page;

public interface PetService extends CrudService<Pet> {

    Page<Pet> findAllByPetGuardianId(Long id, int index, int itemsPerPage);

    boolean petGuardianHasPet(Long petId);
}
