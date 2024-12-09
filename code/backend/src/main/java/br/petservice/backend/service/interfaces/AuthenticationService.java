package br.petservice.backend.service.interfaces;

import br.petservice.backend.model.abstracts.User;
import br.petservice.backend.model.dto.LoginUserDto;
import org.jetbrains.annotations.NotNull;


public interface AuthenticationService<T extends User> {

    T register(@NotNull T registerDto);

    T authenticate(@NotNull LoginUserDto loginUserDto);
}