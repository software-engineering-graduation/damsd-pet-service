package br.petservice.backend.service.interfaces;


import br.petservice.backend.model.abstracts.User;


public interface UserService<T extends User> extends CrudService<T>, AuthenticationService<T> {
}
