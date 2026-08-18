package za.ac.cput.service;

import za.ac.cput.domain.Review;

import java.util.List;

public interface IReviewService extends IService<Review, Long> {
    List<Review> getByProductId(Long productId);

    List<Review> getAll();
}
