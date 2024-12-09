package br.petservice.backend.configuration;

import br.petservice.backend.model.abstracts.User;
import br.petservice.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

/**
 * Configuração da aplicação para autenticação e segurança.
 * Esta classe contém a configuração necessária para a autenticação de usuários,
 * incluindo a definição de um UserDetailsService, um PasswordEncoder e um
 * AuthenticationProvider.
 */
@Configuration
@AllArgsConstructor
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class BackendApplicationConfig {

    private final UserRepository<User> userRepository;

    /**
     * Define um {@link UserDetailsService} que carrega os detalhes do usuário
     * a partir do repositório de usuários.
     *
     * @return Um UserDetailsService que busca usuários pelo e-mail.
     * @throws UsernameNotFoundException Se o usuário não for encontrado.
     */
    @Bean
    UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    /**
     * Define um {@link PasswordEncoder} que usa o algoritmo BCrypt para codificar senhas.
     *
     * @return Um PasswordEncoder que implementa BCrypt.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define um {@link AuthenticationManager} para gerenciar a autenticação de usuários.
     *
     * @param authenticationConfiguration A configuração de autenticação.
     * @return Um AuthenticationManager para gerenciar a autenticação.
     * @throws Exception Se ocorrer um erro durante a criação do AuthenticationManager.
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Define um {@link AuthenticationProvider} que usa o UserDetailsService e
     * o PasswordEncoder configurados.
     *
     * @return Um AuthenticationProvider que gerencia a autenticação via DAO.
     */
    @Bean
    AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }
}
