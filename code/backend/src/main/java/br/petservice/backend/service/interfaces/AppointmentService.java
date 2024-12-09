package br.petservice.backend.service.interfaces;

import br.petservice.backend.model.Appointment;
import br.petservice.backend.model.dto.AppointmentRequestDTO;

public interface AppointmentService extends CrudService<Appointment> {

    Appointment create(AppointmentRequestDTO appointmentRequestDTO);
}
