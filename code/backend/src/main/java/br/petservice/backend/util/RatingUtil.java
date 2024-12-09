package br.petservice.backend.util;

import br.petservice.backend.model.Review;
import br.petservice.backend.model.abstracts.User;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.experimental.UtilityClass;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;

/**
 * Classe utilitária responsável por métodos de cálculo de avaliação.
 */
@UtilityClass
public final class RatingUtil {

    /**
     * Preenche a avaliação de cada usuário com base nas avaliações recebidas.
     *
     * @param users   Lista de usuários.
     * @param reviews Lista de avaliações.
     */
    public static void fillRating(List<? extends User> users, List<Review> reviews){
        Map<Long, List<Review>> reviewsByUserId = reviews.stream().collect(groupingBy(Review::getReviewedUserId));
        users.stream().filter(Objects::nonNull).forEach(user -> setRating(user, reviewsByUserId.getOrDefault(user.getId(), emptyList())));
    }

    /**
     * Calcula e preenche a média de avaliação de um usuário.
     *
     * @param user   Usuário.
     * @param reviews Array de avaliações.
     */
    private void setRating(User user, List<Review> reviews) {
        user.setAverageRating(reviews.stream()
                .filter(Objects::nonNull).mapToDouble(Review::getRating).average().orElse(0));
    }
}
