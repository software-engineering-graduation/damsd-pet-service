package br.petservice.backend.service;

import br.petservice.backend.model.Appointment;
import br.petservice.backend.model.PetGuardian;
import br.petservice.backend.model.ServiceProvided;
import br.petservice.backend.model.dto.AppointmentDto;
import br.petservice.backend.model.dto.AppointmentRequestDTO;
import br.petservice.backend.repository.AppointmentRepository;
import br.petservice.backend.service.interfaces.ServiceProvidedService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.util.List;

import static br.petservice.backend.enums.ExceptionMessagesEnum.MSG_APPOINTMENT_FOUND_EXCEPTION;
import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;

@Slf4j
@Service
@AllArgsConstructor
public class AppointmentServiceImpl {

    private final PetServiceImpl petService;
    private final PetGuardianServiceImpl petGuardianService;
    private final PetEstablishmentServiceImpl petEstablishmentService;
    private final AppointmentRepository appointmentRepository;
    private final ServiceProvidedService serviceProvidedService;

    public AppointmentDto convertToDto(Appointment appointment) {
        return AppointmentDto.builder()
                .id(appointment.getId())
                .petGuardian(petGuardianService.findById(appointment.getPetGuardianId()))
                .pet(isNull(appointment.getPetId()) ? null : petService.findById(appointment.getPetId()))
                .petEstablishment(petEstablishmentService.findById(appointment.getPetEstablishmentId()))
                .appointmentServices(appointment.getServicesProvided().stream().map(service -> serviceProvidedService.findById(service.getId())).toList())
                .totalValue(appointment.getTotalValue())
                .dateTime(appointment.getDateTime())
                .build();
    }


    public Appointment findById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MSG_APPOINTMENT_FOUND_EXCEPTION.getMessage()));
    }

    public List<Appointment> findAllByPetGuardianId(Long id) {
        return appointmentRepository.findAllByPetGuardianId(id)
                .orElseThrow(() -> new EntityNotFoundException(MSG_APPOINTMENT_FOUND_EXCEPTION.getMessage()));
    }

    public List<Appointment> findAllByPetEstablishmentId(Long id) {
        return appointmentRepository.findAllByPetEstablishmentId(id)
                .orElseThrow(() -> new EntityNotFoundException(MSG_APPOINTMENT_FOUND_EXCEPTION.getMessage()));
    }


    public Page<Appointment> findAll(int index, int itemsPerPage) {
        return appointmentRepository.findAll(PageRequest.of(index, itemsPerPage));
    }


    public Appointment create(Appointment entity) {
        return null;
    }


    public Appointment create(AppointmentRequestDTO entity) {

        if (entity.getDateTime().isBefore(now()))
            throw new DateTimeException("Data inválida");

        List<ServiceProvided> serviceProvided = entity.getServicesProvided().stream().map(
                serviceProvidedService::findById).toList();

        Appointment appointment = Appointment.builder()
                .petGuardianId(entity.getPetGuardianId())
                .petEstablishmentId(entity.getPetEstablishmentId())
                .petId(entity.getPetId())
                .totalValue(entity.getTotalValue())
                .dateTime(entity.getDateTime())
                .servicesProvided(serviceProvided)
                .totalValue(serviceProvided.stream().mapToDouble(ServiceProvided::getValue).sum())
                .build();

        return appointmentRepository.save(appointment);
    }


    public Appointment update(Long id, AppointmentRequestDTO entity) {

        Appointment appointment = findById(id);

        if (entity.getDateTime().isBefore(now()))
            throw new DateTimeException("Data inválida");

        List<ServiceProvided> serviceProvided = entity.getServicesProvided().stream().map(
                serviceProvidedService::findById).toList();

        appointment.setServicesProvided(
                entity.getServicesProvided().stream().map(serviceProvidedService::findById).toList());
        appointment.setDateTime(entity.getDateTime());
        appointment.setPetEstablishmentId(entity.getPetEstablishmentId());
        appointment.setPetGuardianId(entity.getPetGuardianId());
        appointment.setTotalValue(serviceProvided.stream().mapToDouble(ServiceProvided::getValue).sum());

        return appointmentRepository.save(appointment);
    }

    public void delete(Long id) {

        findById(id);

        appointmentRepository.deleteById(id);
    }
}
