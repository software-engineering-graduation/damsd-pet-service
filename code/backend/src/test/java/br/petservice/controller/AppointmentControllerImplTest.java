package br.petservice.controller;

import br.petservice.backend.controller.AppointmentControllerImpl;
import br.petservice.backend.model.Appointment;
import br.petservice.backend.model.dto.AppointmentDto;
import br.petservice.backend.model.dto.AppointmentRequestDTO;
import br.petservice.backend.service.AppointmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentControllerImplTest {

    @Mock
    private AppointmentServiceImpl appointmentService;

    @InjectMocks
    private AppointmentControllerImpl appointmentController;

    @Test
    void testFindById() {
        Long id = 1L;
        Appointment appointment = new Appointment();
        AppointmentDto appointmentDto = AppointmentDto.builder().build();
        when(appointmentService.findById(id)).thenReturn(appointment);
        when(appointmentService.convertToDto(appointment)).thenReturn(appointmentDto);

        ResponseEntity<AppointmentDto> response = appointmentController.findById(id);

        assertEquals(appointmentDto, response.getBody());
    }

    @Test
    void testFindAll() {
        int index = 0;
        int itemsPerPage = 10;
        Page<Appointment> page = Page.empty();
        Page<AppointmentDto> pageDto = Page.empty();
        when(appointmentService.findAll(index, itemsPerPage)).thenReturn(page);
//        when(page.map(appointmentService::convertToDto)).thenReturn(pageDto);

        ResponseEntity<Page<AppointmentDto>> response = appointmentController.findAll(index, itemsPerPage);

        assertEquals(pageDto, response.getBody());
    }

    @Test
    void testCreate() {
        AppointmentRequestDTO dto = new AppointmentRequestDTO();
        Appointment appointment = new Appointment();
        AppointmentDto appointmentDto = AppointmentDto.builder().build();
        when(appointmentService.create(dto)).thenReturn(appointment);
        when(appointmentService.convertToDto(appointment)).thenReturn(appointmentDto);

        ResponseEntity<AppointmentDto> response = appointmentController.create(dto);

        assertEquals(appointmentDto, response.getBody());
    }

    @Test
    void testUpdate() {
        Long id = 1L;
        AppointmentRequestDTO dto = new AppointmentRequestDTO();
        Appointment appointment = new Appointment();
        AppointmentDto appointmentDto = AppointmentDto.builder().build();
        when(appointmentService.update(id, dto)).thenReturn(appointment);
        when(appointmentService.convertToDto(appointment)).thenReturn(appointmentDto);

        ResponseEntity<AppointmentDto> response = appointmentController.update(id, dto);

        assertEquals(appointmentDto, response.getBody());
    }

    @Test
    void testDelete() {
        Long id = 1L;

        ResponseEntity<Void> response = appointmentController.delete(id);

        assertEquals(204, response.getStatusCodeValue());
    }
}