package br.petservice.backend.service.interfaces;

import br.petservice.backend.model.ServiceProvided;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ServiceProvidedService extends CrudService<ServiceProvided> {

    List<ServiceProvided> create(List<ServiceProvided> servicesProvided);

    boolean petEstablishmentHasServiceProvided(Long serviceProvidedId);

    Page<ServiceProvided> findAllByPetEstablishmentId(Long id, int index, int itemsPerPage);
}
