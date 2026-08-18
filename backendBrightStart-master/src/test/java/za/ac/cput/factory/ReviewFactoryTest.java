package za.ac.cput.factory;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Customer;
import za.ac.cput.domain.Product;
import za.ac.cput.domain.Review;

import static org.junit.jupiter.api.Assertions.*;

class ReviewFactoryTest {

    private static final Customer customer1 = new Customer.Builder()
            .setUserId(1L)
            .setUsername("john_doe")
            .setPassword("password123")
            .setEmail("john@example.com")
            .setFirstName("John")
            .setLastName("Doe")
            .setAddress("123 Main Street")
            .setPhoneNumber("0123456789")
            .build();

    private static final Product product1 = new Product.Builder()
            .setProductId(1L)
            .setName("Anime Hoodie")
            .setPrice(599.99)
            .build();

    public static final Review review1 = ReviewFactory.createReview(
            5, "Excellent quality and design!", product1, customer1);

    public static final Review review2 = ReviewFactory.createReview(
            6, "Too good!", product1, customer1);

    public static final Review review3 = ReviewFactory.createReview(
            4, "", product1, customer1);

    public static final Review review4 = ReviewFactory.createReview(
            4, "Good product!", product1, null);

    public static final Review review5 = ReviewFactory.createReview(
            4, "Nice!", null, customer1);

    @Test
    @Order(1)
    public void testCreateReviewSuccess() {
        assertNotNull(review1);
        System.out.println(review1);
    }

    @Test
    @Order(2)
    public void testCreateReviewInvalidRating() {
        assertNull(review2);
        System.out.println("Invalid rating review: " + review2);
    }
    @Test
    @Order(3)
    public void testCreateReviewEmptyComment() {
        assertNotNull(review3);
        System.out.println("Empty comment review: " + review3);
    }


    @Test
    @Order(4)
    public void testCreateReviewNullCustomer() {
        assertNull(review4);
        System.out.println("Null customer review: " + review4);
    }

    @Test
    @Order(5)
    public void testCreateReviewNullProduct() {
        assertNull(review5);
        System.out.println("Null product review: " + review5);
    }

    @Test
    @Disabled
    @Order(6)
    public void testNotImplemented() {
        // Placeholder for future tests
    }
}
