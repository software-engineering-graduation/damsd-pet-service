package br.petservice.service;

import br.petservice.backend.model.Appointment;
import br.petservice.backend.model.ServiceProvided;
import br.petservice.backend.model.dto.AppointmentDto;
import br.petservice.backend.model.dto.AppointmentRequestDTO;
import br.petservice.backend.repository.AppointmentRepository;
import br.petservice.backend.service.AppointmentServiceImpl;
import br.petservice.backend.service.PetEstablishmentServiceImpl;
import br.petservice.backend.service.PetGuardianServiceImpl;
import br.petservice.backend.service.PetServiceImpl;
import br.petservice.backend.service.interfaces.ServiceProvidedService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PetServiceImpl petService;

    @Mock
    private PetGuardianServiceImpl petGuardianService;

    @Mock
    private PetEstablishmentServiceImpl petEstablishmentService;

    @Mock
    private ServiceProvidedService serviceProvidedService;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private AppointmentRequestDTO appointmentRequestDTO;
    private Appointment appointment;
    private ServiceProvided serviceProvided;

    @BeforeEach
    public void setUp() {
        appointmentRequestDTO = new AppointmentRequestDTO();
        appointmentRequestDTO.setPetGuardianId(1L);
        appointmentRequestDTO.setPetEstablishmentId(1L);
        appointmentRequestDTO.setDateTime(LocalDateTime.now().plusDays(1));
        appointmentRequestDTO.setServicesProvided(List.of(1L, 2L));
        appointmentRequestDTO.setPetId(1L);
        appointmentRequestDTO.setTotalValue(10.0);

        serviceProvided = new ServiceProvided();
        serviceProvided.setValue(10.0);

        appointment = new Appointment();
        appointment.setId(1L);
        appointment.setPetId(1L);
        appointment.setTotalValue(10.0);
        appointment.setPetGuardianId(1L);
        appointment.setPetEstablishmentId(1L);
        appointment.setDateTime(LocalDateTime.now().plusDays(1));
        appointment.setServicesProvided(List.of(serviceProvided, serviceProvided));
    }

    @Test
    public void testFindById() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        Appointment result = appointmentService.findById(1L);

        assertEquals(appointment, result);
    }

    @Test
    public void testDeleteAppointment() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        appointmentService.delete(1L);

        verify(appointmentRepository).deleteById(1L);
    }


    @Test
    public void testConvertToDto() {
        when(petGuardianService.findById(1L)).thenReturn(null);
        when(petService.findById(any())).thenReturn(null);
        when(petEstablishmentService.findById(1L)).thenReturn(null);
        when(serviceProvidedService.findById(any())).thenReturn(null);

        AppointmentDto dto = appointmentService.convertToDto(appointment);

        assertNotNull(dto);
        assertEquals(appointment.getId(), dto.id());
    }


    @Test
    public void testFindByIdNotFound() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> appointmentService.findById(1L));
    }

    @Test
    public void testFindAllByPetGuardianId() {
        when(appointmentRepository.findAllByPetGuardianId(1L)).thenReturn(Optional.of(List.of(appointment)));

        List<Appointment> result = appointmentService.findAllByPetGuardianId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindAllByPetEstablishmentId() {
        when(appointmentRepository.findAllByPetEstablishmentId(1L)).thenReturn(Optional.of(List.of(appointment)));

        List<Appointment> result = appointmentService.findAllByPetEstablishmentId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindAll() {
        Page<Appointment> page = new PageImpl<>(List.of(appointment));
        when(appointmentRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        Page<Appointment> result = appointmentService.findAll(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testCreate() {
        when(serviceProvidedService.findById(any())).thenReturn(serviceProvided);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        Appointment result = appointmentService.create(appointmentRequestDTO);

        assertNotNull(result);
        assertEquals(appointment.getId(), result.getId());
    }

    @Test
    public void testCreateWithInvalidDate() {
        appointmentRequestDTO.setDateTime(LocalDateTime.now().minusDays(1));

        assertThrows(DateTimeException.class, () -> appointmentService.create(appointmentRequestDTO));
    }

    @Test
    public void testUpdate() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(serviceProvidedService.findById(any())).thenReturn(serviceProvided);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        Appointment result = appointmentService.update(1L, appointmentRequestDTO);

        assertNotNull(result);
        assertEquals(appointment.getId(), result.getId());
    }

    @Test
    public void testUpdateWithInvalidDate() {
        appointmentRequestDTO.setDateTime(LocalDateTime.now().minusDays(1));

        assertThrows(EntityNotFoundException.class, () -> appointmentService.update(1L, appointmentRequestDTO));
    }

    @Test
    public void testDelete() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        doNothing().when(appointmentRepository).deleteById(1L);

        appointmentService.delete(1L);

        verify(appointmentRepository, times(1)).deleteById(1L);
    }
}