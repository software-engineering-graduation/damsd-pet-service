package br.petservice.service;

import br.petservice.backend.model.PetGuardian;
import br.petservice.backend.model.dto.LoginUserDto;
import br.petservice.backend.repository.UserRepository;
import br.petservice.backend.service.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static br.petservice.backend.enums.ExceptionMessagesEnum.MSG_USER_NOT_FOUND_EXCEPTION;
import static br.petservice.backend.enums.RoleEnum.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private UserRepository<PetGuardian> userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl<PetGuardian> authenticationService;

    @Test
    void testRegister() {
        PetGuardian user = new PetGuardian();
        user.setPassword("password");
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(PetGuardian.class))).thenReturn(user);

        PetGuardian result = authenticationService.register(user);

        assertNotNull(result);
        assertEquals(USER.getRole(), result.getRole());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testAuthenticate() {
        LoginUserDto loginUserDto = new LoginUserDto("email", "password");
        PetGuardian user = new PetGuardian();
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

        PetGuardian result = authenticationService.authenticate(loginUserDto);

        assertNotNull(result);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByEmail(loginUserDto.email());
    }

    @Test
    void testAuthenticateUserNotFound() {
        LoginUserDto loginUserDto = new LoginUserDto("email", "password");
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            authenticationService.authenticate(loginUserDto);
        });

        assertEquals(MSG_USER_NOT_FOUND_EXCEPTION.getMessage(), exception.getMessage());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByEmail(loginUserDto.email());
    }
}