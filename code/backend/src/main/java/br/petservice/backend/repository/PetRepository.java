package br.petservice.backend.repository;

import br.petservice.backend.model.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {

    Optional<Page<Pet>> findAllByPetGuardianId(Long id, Pageable pageable);
}
