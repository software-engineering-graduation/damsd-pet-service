package br.petservice.backend.util;

import lombok.experimental.UtilityClass;

import java.util.function.Consumer;

import static java.util.Objects.nonNull;

/**
 * Classe utilitária que fornece métodos para configurar valores de forma condicional.
 * A classe `SetUtils` contém métodos auxiliares para definir valores de atributos
 * apenas quando esses valores não são nulos. Isso serve para evitar sobrescrições
 * indesejadas de valores que já estão configurados.
 */
@UtilityClass
public final class SetUtil {

    /**
     * Define o valor usando o setter fornecido apenas se o valor não for nulo.
     * Este método usa um {@link Consumer} como setter para atribuir o valor fornecido
     * ao field desejado, desde que o valor não seja nulo. Isso ajuda a garantir que
     * os campos existentes não sejam sobrescritos por valores nulos.
     *
     * @param <T>    O tipo do valor a ser configurado.
     * @param setter O método setter ou qualquer {@link Consumer} que manipule o valor.
     * @param value  O valor a ser definido se não for nulo.
     */
    public static <T> void setIfNotNull(Consumer<T> setter, T value) {
        if (nonNull(value))
            setter.accept(value);
    }
}
