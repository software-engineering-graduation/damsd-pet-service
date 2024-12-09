package br.petservice.backend.configuration;

import br.petservice.backend.model.Address;
import br.petservice.backend.model.PetEstablishment;
import br.petservice.backend.model.PetGuardian;
import br.petservice.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static br.petservice.backend.enums.RoleEnum.ADMIN;

/**
 * A classe {@code AdminUserInitializer} é responsável por criar usuários administrativos padrão
 * na inicialização da aplicação, caso eles ainda não existam no banco de dados.
 * <p>
 * Ao iniciar a aplicação, ela verifica se já existem usuários administrativos específicos
 * (um para {@code PetGuardian} e outro para {@code PetEstablishment}) com e-mails predefinidos.
 * Se não forem encontrados, a classe cria os usuários e os salva no banco de dados.
 * <p>
 * Esta classe implementa {@code CommandLineRunner}, o que permite que ela seja executada
 * logo após a inicialização do contexto do Spring.
 */
@Component
@AllArgsConstructor
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepository<PetGuardian> petGuardianUserRepository;
    private final UserRepository<PetEstablishment> petEstablishmentUserRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Método executado automaticamente após a inicialização da aplicação.
     * Verifica se os usuários administrativos não existem no banco e os cria caso necessário.
     *
     * @param args argumentos da linha de comando (não usados neste caso)
     */
    @Override
    public void run(String... args) {

        if (petGuardianUserRepository.findByEmail("admintutor@admin.com").isEmpty()) {
            PetGuardian adminPetGuardian = new PetGuardian();

            adminPetGuardian.setCpf("44697634039");
            adminPetGuardian.setFullName("Usuário administrador tutor");
            adminPetGuardian.setBirthDate(LocalDate.of(2000, 10, 1));
            adminPetGuardian.setAddress(createAddress());
            adminPetGuardian.setRole(ADMIN.getRole());
            adminPetGuardian.setPhoneNumber("44697634039");
            adminPetGuardian.setEmail("admintutor@admin.com");
            adminPetGuardian.setPassword(passwordEncoder.encode("admin"));

            petGuardianUserRepository.save(adminPetGuardian);
        }

        if (petEstablishmentUserRepository.findByEmail("adminestabelecimento@admin.com").isEmpty()) {
            PetEstablishment adminPetEstablishment = new PetEstablishment();

            adminPetEstablishment.setCnpj("25466584000116");
            adminPetEstablishment.setBusinessName("Usuário administrador estabelecimento");
            adminPetEstablishment.setAddress(createAddress());
            adminPetEstablishment.setRole(ADMIN.getRole());
            adminPetEstablishment.setPhoneNumber("44697634039");
            adminPetEstablishment.setEmail("adminestabelecimento@admin.com");
            adminPetEstablishment.setPassword(passwordEncoder.encode("admin"));

            petEstablishmentUserRepository.save(adminPetEstablishment);
        }
    }

    /**
     * Método auxiliar para criar o endereço padrão usado por ambos os usuários administrativos.
     *
     * @return uma instância de {@link Address} contendo o endereço padrão.
     */
    private Address createAddress() {
        return Address.builder()
                .city("cidade")
                .uf("mg")
                .state("estado")
                .neighborhood("bairro")
                .postalCode("00000000")
                .region("regiao")
                .street("rua")
                .streetNumber(10)
                .additionalInformation("informacao adicional")
                .build();
    }
}
