package br.petservice.backend.model.abstracts;

import br.petservice.backend.model.Address;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static jakarta.persistence.CascadeType.ALL;
import static java.util.Collections.singleton;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Classe abstrata que representa um usuário no sistema, contendo informações básicas como endereço, avaliações, email e telefone.")
public abstract class User extends BaseEntity implements UserDetails {

    @ToString.Exclude
    @OneToOne(cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    @Schema(description = "Endereço do usuário.")
    private Address address;

    @Column(nullable = false)
    @JsonProperty("role")
    @Schema(description = "Função do usuário no sistema (por exemplo, ADMIN ou USER).", example = "ADMIN")
    private String role;

    @Column(nullable = false)
    @Pattern(regexp = "^[0-9]{11}$", message = "formato esperado: 11 dígitos numéricos consecutivos")
        @Schema(description = "Número de telefone do usuário com 11 dígitos numéricos consecutivos.", example = "31999999999")
    private String phoneNumber;

    @Column(unique = true, nullable = false, length = 100)
    @Pattern(regexp = "^[A-Za-z0-9.]+@[A-Za-z0-9]+\\.[A-Za-z]+\\.?([A-Za-z]+)?$", message = "formato esperado: xxx@xxx.com.br ou xxx@xxx.com")
    @Schema(description = "Endereço de email único do usuário.", example = "usuario@exemplo.com")
    private String email;

    @Column(nullable = false)
    @Size(min = 6, max = 60, message = "a senha deve ter de 6 a 60 caracteres")
    @JsonProperty(access = WRITE_ONLY)
    @Schema(description = "Senha do usuário, deve ter entre 6 e 60 caracteres.", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    @Column
    @Schema(description = "Url da imagem do usuário", example = "https://i.ibb.co/jhdnKs7/user.jpg")
    private String image;

    @Transient
    @Schema(description = "Avaliação média do usuário, não persistida no banco de dados")
    private Double averageRating;

    @Override
    @Schema(description = "Autoridades associadas ao usuário, baseadas em sua função (role).", example = "[\"ADMIN\"]")
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return singleton(new SimpleGrantedAuthority(role.toUpperCase()));
    }

    @Override
    @Schema(description = "Retorna o email como o nome de usuário.", example = "usuario@exemplo.com")
    public String getUsername() {
        return getEmail();
    }

}
