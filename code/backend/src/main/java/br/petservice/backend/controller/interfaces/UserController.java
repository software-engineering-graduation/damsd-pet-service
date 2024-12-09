package br.petservice.backend.controller.interfaces;


import br.petservice.backend.model.abstracts.User;
import br.petservice.backend.model.dto.LoginResponseDto;
import br.petservice.backend.model.dto.LoginUserDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Interface que define os endpoints de controle para operações relacionadas a usuários.
 * Estende a interface {@link CrudController}, fornecendo operações CRUD genéricas e
 * adiciona endpoints específicos para autenticação e recuperação de informações do usuário autenticado.
 *
 * @param <T> Tipo genérico que deve estender {@link User}, permitindo a reutilização desta interface
 *            para diferentes tipos de usuários no sistema.
 */
public interface UserController<T extends User, D> extends CrudController<T, D> {

    @GetMapping("/me")
    ResponseEntity<D> findMe();

    @Override
    @PostMapping("/auth")
    ResponseEntity<D> create(@Valid @RequestBody T registerUserDto);

    @PostMapping("/auth/authenticate")
    ResponseEntity<LoginResponseDto> authenticate(@Valid @RequestBody LoginUserDto loginUserDto);
}
