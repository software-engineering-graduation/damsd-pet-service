package br.petservice.backend.service;

import br.petservice.backend.model.abstracts.User;
import br.petservice.backend.model.dto.LoginUserDto;
import br.petservice.backend.repository.UserRepository;
import br.petservice.backend.service.interfaces.AuthenticationService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static br.petservice.backend.enums.ExceptionMessagesEnum.MSG_USER_NOT_FOUND_EXCEPTION;
import static br.petservice.backend.enums.RoleEnum.USER;

/**
 * Implementação do serviço de autenticação para usuários.
 * <p>
 * Esta classe é responsável pelo registro de novos usuários e autenticação de
 * usuários existentes, utilizando {@link UserRepository}, {@link PasswordEncoder} e
 * {@link AuthenticationManager} para gerenciar as credenciais de segurança.
 *
 * @param <T> Tipo genérico que estende a entidade {@link User}.
 */
@Service
@AllArgsConstructor
public class AuthenticationServiceImpl<T extends User> implements AuthenticationService<T> {

    private final UserRepository<T> userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Registra um novo usuário.
     * O método salva um novo usuário no banco de dados, codifica a senha e define a
     * função padrão como "USER".
     *
     * @param registerDto Objeto contendo as informações do usuário a ser registrado.
     * @return O usuário recém-registrado.
     */
    @Override
    public T register(@NotNull T registerDto) {

        registerDto.setRole(USER.getRole());
        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        return userRepository.save(registerDto);
    }

    /**
     * Autentica um usuário existente.
     * O método verifica as credenciais do usuário (e-mail e senha), e, se corretas,
     * retorna o usuário autenticado. Caso contrário, lança uma exceção se o usuário não for encontrado.
     *
     * @param loginUserDto DTO contendo as credenciais de login do usuário (e-mail e senha).
     * @return O usuário autenticado.
     * @throws UsernameNotFoundException Se o usuário não for encontrado no banco de dados.
     */
    @Override
    public T authenticate(@NotNull LoginUserDto loginUserDto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password()));

        return userRepository.findByEmail(loginUserDto.email())
                .orElseThrow(() -> new UsernameNotFoundException(MSG_USER_NOT_FOUND_EXCEPTION.getMessage()));
    }
}
