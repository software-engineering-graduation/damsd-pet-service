package br.petservice.backend.controller;

import br.petservice.backend.controller.interfaces.UserController;
import br.petservice.backend.model.PetGuardian;
import br.petservice.backend.model.abstracts.User;
import br.petservice.backend.model.dto.LoginResponseDto;
import br.petservice.backend.model.dto.LoginUserDto;
import br.petservice.backend.model.dto.PetGuardianDto;
import br.petservice.backend.security.JwtService;
import br.petservice.backend.service.interfaces.UserService;
import br.petservice.backend.util.DTOConverterUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.petservice.backend.util.DTOConverterUtil.toDto;
import static br.petservice.backend.util.SecurityUtil.getAuthenticatedPetGuardian;
import static org.springframework.http.HttpStatus.CREATED;

/**
 * Implementação do controlador responsável por gerenciar os usuários do tipo {@link PetGuardian}.
 */
@Slf4j
@RestController
@Validated
@RequestMapping(value = "/pet-guardian")
@AllArgsConstructor
@Tag(name = "PetGuardianController", description = "API para operações de usuários do tipo PetGuardian.")
public class PetGuardianControllerImpl implements UserController<PetGuardian, PetGuardianDto> {

    private final UserService<PetGuardian> userService;
    private final JwtService jwtService;

    /**
     * Retorna as informações do usuário atualmente autenticado.
     *
     * @return {@link ResponseEntity} contendo o DTO do usuário autenticado.
     */
    @Override
    @Operation(summary = "Buscar informações do usuário autenticado", description = "Retorna os detalhes do PetGuardian autenticado.")
    public ResponseEntity<PetGuardianDto> findMe() {

        log.info("findMe() - recebendo solicitação para buscar informações do usuário atual.");

        return ResponseEntity.ok(DTOConverterUtil.toDto(getAuthenticatedPetGuardian()));
    }

    /**
     * Retorna as informações de um usuário com base no ID fornecido.
     *
     * @param id ID do usuário a ser buscado.
     * @return {@link ResponseEntity} contendo o DTO do usuário encontrado.
     */
    @Override
    @Operation(summary = "Buscar usuário por ID", description = "Retorna os detalhes do PetGuardian baseado no ID fornecido.")
    public ResponseEntity<PetGuardianDto> findById(Long id) {

        log.info("findById() - recebendo solicitação para buscar informações do usuário com id: {}.", id);

        return ResponseEntity.ok(DTOConverterUtil.toDto(userService.findById(id)));
    }

    /**
     * Retorna uma página de usuários com paginação baseada no índice e número de itens por página.
     *
     * @param index        Índice da página.
     * @param itemsPerPage Número de itens por página.
     * @return {@link ResponseEntity} contendo uma página de usuários em formato DTO.
     */
    @Override
    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista paginada de todos os PetGuardians.")
    public ResponseEntity<Page<PetGuardianDto>> findAll(int index, @PathVariable int itemsPerPage) {

        log.info("findAll() - Buscando todos os tutores na página {} com {} itens por página.", index, itemsPerPage);

        return ResponseEntity.ok(userService.findAll(index, itemsPerPage).map(DTOConverterUtil::toDto));
    }

    /**
     * Registra um novo usuário do tipo {@link PetGuardian}.
     *
     * @param registerUserDto Objeto contendo os dados do usuário a ser registrado.
     * @return {@link ResponseEntity} contendo o DTO do usuário registrado e o status HTTP 201 (Created).
     */
    @Override
    @Operation(summary = "Registrar um novo usuário", description = "Registra um novo PetGuardian.")
    public ResponseEntity<PetGuardianDto> create(PetGuardian registerUserDto) {

        log.info("create() - recebendo solicitação para registrar usuário: {}.", registerUserDto);

        return ResponseEntity.status(CREATED).body(DTOConverterUtil.toDto(userService.create(registerUserDto)));
    }

    /**
     * Atualiza as informações de um usuário existente.
     *
     * @param id     ID do usuário a ser atualizado.
     * @param entity Objeto contendo as novas informações do usuário.
     * @return {@link ResponseEntity} contendo o DTO do usuário atualizado.
     */
    @Override
    @Operation(summary = "Atualizar um usuário existente", description = "Atualiza as informações de um PetGuardian existente.")
    public ResponseEntity<PetGuardianDto> update(Long id, PetGuardian entity) {

        log.info("update() - recebendo solicitação para atualizar o usuário com id: {}.", id);

        return ResponseEntity.ok().body(DTOConverterUtil.toDto(userService.update(id, entity)));
    }

    /**
     * Deleta um usuário com base no ID fornecido.
     *
     * @param id ID do usuário a ser deletado.
     * @return {@link ResponseEntity} sem conteúdo com status HTTP 204 (No Content).
     */
    @Override
    @Operation(summary = "Deletar um usuário", description = "Deleta um PetGuardian existente baseado no ID fornecido.")
    public ResponseEntity<Void> delete(@Parameter(description = "ID do usuário a ser deletado", required = true) Long id) {

        log.info("delete() - recebendo solicitação para deletar o usuário com id: {}.", id);

        userService.delete(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Autentica um usuário baseado nas credenciais fornecidas.
     *
     * @param loginUserDto DTO contendo as credenciais do usuário (e-mail e senha).
     * @return {@link ResponseEntity} contendo o DTO de resposta de login com o token JWT.
     */
    @Override
    @Operation(summary = "Autenticar um usuário", description = "Autentica um PetGuardian usando email e senha e retorna um token JWT.")
    public ResponseEntity<LoginResponseDto> authenticate(@NotNull LoginUserDto loginUserDto) {

        log.info("authenticate() - recebendo solicitação de autenticação para usuário: {}.", loginUserDto.email());

        User user = userService.authenticate(loginUserDto);
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(toDto(user, token, jwtService.getJwtExpiration()));
    }
}
