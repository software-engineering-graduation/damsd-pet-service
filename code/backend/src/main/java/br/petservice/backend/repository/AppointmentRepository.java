package br.petservice.backend.repository;

import br.petservice.backend.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<List<Appointment>> findAllByPetGuardianId(Long id);

    Optional<List<Appointment>> findAllByPetEstablishmentId(Long id);
}
