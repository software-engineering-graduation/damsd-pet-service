package br.petservice.backend.repository;

import br.petservice.backend.model.ServiceProvided;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceProvidedRepository extends JpaRepository<ServiceProvided, Long> {

    Optional<Page<ServiceProvided>> findAllByPetEstablishmentId(Long id, Pageable pageable);
}
