package br.petservice.backend.util;

import br.petservice.backend.model.PetEstablishment;
import br.petservice.backend.model.PetGuardian;
import lombok.experimental.UtilityClass;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

/**
 * Classe utilitária que fornece métodos auxiliares relacionados à segurança, especificamente para
 * acessar o usuário autenticado no contexto de segurança do Spring.
 *
 * <p>Esta classe utiliza o padrão {@link UtilityClass} do Lombok, o que significa que ela não
 * pode ser instanciada e todos os seus métodos são estáticos. Seu principal objetivo é fornecer
 * um método para acessar o {@link PetGuardian} atualmente autenticado de maneira centralizada e
 * reutilizável.</p>
 *
 * <p>Exemplo de uso:</p>
 * <pre>{@code
 * PetGuardian authenticatedPetGuardian = SecurityUtil.getAuthenticatedPetGuardian();
 * }</pre>
 */
@UtilityClass
public class SecurityUtil {

    /**
     * Obtém o {@link PetGuardian} atualmente autenticado do contexto de segurança do Spring.
     *
     * <p>Este método acessa o {@link org.springframework.security.core.context.SecurityContextHolder}
     * para obter a autenticação atual e retorna o principal como um objeto {@link PetGuardian}.</p>
     *
     * @return o {@link PetGuardian} autenticado.
     * @throws ClassCastException se o principal não for uma instância de {@link PetGuardian}.
     */
    public static PetGuardian getAuthenticatedPetGuardian() {
        return (PetGuardian) getContext().getAuthentication().getPrincipal();
    }

    /**
     * Obtém o {@link PetEstablishment} atualmente autenticado do contexto de segurança do Spring.
     *
     * <p>Este método acessa o {@link org.springframework.security.core.context.SecurityContextHolder}
     * para obter a autenticação atual e retorna o principal como um objeto {@link PetEstablishment}.</p>
     *
     * @return o {@link PetEstablishment} autenticado.
     * @throws ClassCastException se o principal não for uma instância de {@link PetEstablishment}.
     */
    public static PetEstablishment getAuthenticatedPetEstablishment() {
        return (PetEstablishment) getContext().getAuthentication().getPrincipal();
    }
}
