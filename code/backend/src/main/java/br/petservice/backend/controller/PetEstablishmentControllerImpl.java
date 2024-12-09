package br.petservice.backend.controller;

import br.petservice.backend.controller.interfaces.UserController;
import br.petservice.backend.model.PetEstablishment;
import br.petservice.backend.model.abstracts.LegalEntity;
import br.petservice.backend.model.abstracts.User;
import br.petservice.backend.model.dto.LoginResponseDto;
import br.petservice.backend.model.dto.LoginUserDto;
import br.petservice.backend.model.dto.PetEstablishmentDto;
import br.petservice.backend.security.JwtService;
import br.petservice.backend.service.interfaces.UserService;
import br.petservice.backend.util.DTOConverterUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.petservice.backend.util.DTOConverterUtil.toDto;
import static org.springframework.http.HttpStatus.CREATED;

/**
 * Implementação do controlador responsável por gerenciar os usuários do tipo {@link PetEstablishment}.
 * Esta classe implementa a interface {@link UserController} e oferece operações para registro,
 * autenticação e CRUD completo de um usuário {@link PetEstablishment}.
 * <p>
 * Utiliza o serviço {@link UserService} para realizar as operações e o {@link JwtService} para geração
 * e validação de tokens JWT durante o processo de autenticação.
 */
@Slf4j
@RestController
@Validated
@RequestMapping(value = "/pet-establishment")
@AllArgsConstructor
@Tag(name = "PetEstablishmentController", description = "API para operações de usuários do tipo PetEstablishment.")
public class PetEstablishmentControllerImpl implements UserController<PetEstablishment, PetEstablishmentDto> {

    private final UserService<PetEstablishment> userService;
    private final JwtService jwtService;

    @Override
    @Operation(summary = "Obter informações do usuário autenticado",
            description = "Retorna as informações do usuário atualmente autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informações do usuário autenticado retornadas com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    public ResponseEntity<PetEstablishmentDto> findMe() {

        log.info("findMe() - recebendo solicitação para buscar informações do usuário atual.");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(DTOConverterUtil.toDto((LegalEntity) authentication.getPrincipal()));
    }

    @Override
    @Operation(summary = "Buscar usuário por ID",
            description = "Retorna as informações de um usuário com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<PetEstablishmentDto> findById(Long id) {

        log.info("findById() - recebendo solicitação para buscar informações do usuário com id: {}.", id);

        return ResponseEntity.ok(DTOConverterUtil.toDto(userService.findById(id)));
    }

    @Override
    @Operation(summary = "Buscar todos os usuários",
            description = "Retorna uma página de usuários com paginação baseada no índice e número de itens por página.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de usuários retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    })
    public ResponseEntity<Page<PetEstablishmentDto>> findAll(int index, int itemsPerPage) {

        log.info("findAll() - recebendo solicitação para buscar todos os usuários.");

        return ResponseEntity.ok(userService.findAll(index, itemsPerPage).map(DTOConverterUtil::toDto));
    }

    @Override
    @Operation(summary = "Registrar novo usuário",
            description = "Registra um novo usuário do tipo PetEstablishment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<PetEstablishmentDto> create(@Parameter(description = "Dados do usuário a ser registrado", required = true) PetEstablishment registerUserDto) {

        log.info("create() - recebendo solicitação para registrar usuário: {}.", registerUserDto);

        PetEstablishmentDto registeredUser = DTOConverterUtil.toDto(userService.create(registerUserDto));

        return ResponseEntity.status(CREATED).body(registeredUser);
    }

    @Override
    @Operation(summary = "Atualizar informações de um usuário",
            description = "Atualiza as informações de um usuário existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<PetEstablishmentDto> update(Long id, PetEstablishment entity) {

        log.info("update() - recebendo solicitação para atualizar o usuário com id: {}.", id);

        return ResponseEntity.ok().body(DTOConverterUtil.toDto(userService.update(id, entity)));
    }

    @Override
    @Operation(summary = "Deletar um usuário",
            description = "Deleta um usuário com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Void> delete(@Parameter Long id) {

        log.info("delete() - recebendo solicitação para deletar o usuário com id: {}.", id);

        userService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @Override
    @Operation(summary = "Autenticar um usuário",
            description = "Autentica um usuário baseado nas credenciais fornecidas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    public ResponseEntity<LoginResponseDto> authenticate(@NotNull LoginUserDto loginUserDto) {

        log.info("authenticate() - recebendo solicitação de autenticação para usuário: {}.", loginUserDto.email());

        User user = userService.authenticate(loginUserDto);
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(toDto(user, token, jwtService.getJwtExpiration()));
    }
}
