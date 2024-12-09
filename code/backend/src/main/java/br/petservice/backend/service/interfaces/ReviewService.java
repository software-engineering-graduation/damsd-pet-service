package br.petservice.backend.service.interfaces;

import br.petservice.backend.model.Review;
import org.springframework.data.domain.Page;

import java.util.List;
import org.springframework.data.domain.PageRequest;

public interface ReviewService extends CrudService<Review> {

    Page<Review> findAllByAuthorId(long userId, int index, int itemsPerPage);

    List<Review> findAllByReviewedUserIds(List<Long> reviewedUserIds);
}
