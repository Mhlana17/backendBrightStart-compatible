package za.ac.cput.factory;

import za.ac.cput.domain.Customer;
import za.ac.cput.domain.Product;
import za.ac.cput.domain.Review;
import za.ac.cput.util.Helper;

import java.time.LocalDate;
import java.util.Objects;

public class ReviewFactory {

    public static Review createReview(int rating, String comment,  Product product,  Customer customer){
        if (!Helper.isValidRating(rating) || product == null || customer == null) {

            return null;
        }

        return new Review.Builder()
                .setRating(rating)
                .setComment(comment)
                .setReviewDate(LocalDate.now())
                .setProduct(product)
                .setUser(customer)
                .build();
    }
}
