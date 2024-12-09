package br.petservice.backend.controller.interfaces;

import br.petservice.backend.model.ServiceProvided;
import br.petservice.backend.model.dto.ServiceProvidedDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ServiceProvidedController extends CrudController<ServiceProvided, ServiceProvidedDto> {

    @PostMapping("/list")
    ResponseEntity<List<ServiceProvidedDto>> create(@Valid @RequestBody List<ServiceProvided> servicesProvided);

    @GetMapping("/pet-establishment/{id}/{index}/{itemsPerPage}")
    ResponseEntity<Page<ServiceProvidedDto>> findAllByPetEstablishmentId(@PathVariable Long id,
                                                                         @PathVariable int index,
                                                                         @PathVariable int itemsPerPage);
}
