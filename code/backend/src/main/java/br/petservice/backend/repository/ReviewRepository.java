package br.petservice.backend.repository;

import br.petservice.backend.model.Review;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAllByAuthorId(Long userId, Pageable pageable);

    List<Review> findAllByReviewedUserIdIn(List<Long> reviewedUserIds);

}
