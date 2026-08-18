//package za.ac.cput.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import za.ac.cput.domain.*;
//import za.ac.cput.factory.ReviewFactory;
//import za.ac.cput.service.impl.ProductServiceImpl;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class ReviewServiceTest {
//
//    @Autowired
//    private ReviewService reviewService;
//    @Autowired
//    private CustomerService customerService;
//    @Autowired
//    private ProductServiceImpl productService;
//
//    private Customer customer1;
//    private Product product1;
//    public Review review1;
//
//    @BeforeEach
//    void setUp() {
//        customer1 = new Customer.Builder()
//                .setUsername("john_doe")
//                .setPassword("password123")
//                .setEmail("john@example.com")
//                .setFirstName("John")
//                .setLastName("Doe")
//                .setAddress("123 Main Street")
//                .setPhoneNumber("0123456789")
//                .build();
//
//        product1 = new Product.Builder()
//                .setProductId(1l)
//                .setName("Anime Hoodie")
//                .setPrice(599.99)
//                .build();
//
//        review1 = ReviewFactory.createReview(
//                5, "Excellent quality and design!", product1, customer1);
//
//    }
//
//    @Test
//    void create() {
//        Customer savedCustomer = customerService.create(customer1);
//        Product savedProduct = productService.save(product1);
//
//        review1 = ReviewFactory.createReview(
//                5, "Excellent quality and design!", savedProduct, savedCustomer);
//
//        Review created = reviewService.create(review1);
//        assertNotNull(created);
//        System.out.println(created);
//    }
//
//
//    @Test
//    void read() {
//        Review read = reviewService.read(2L);
//        assertNotNull(read);
//        System.out.println(read);
//    }
//
//    @Test
//    void update() {
//        Customer newCustomer = new Customer.Builder()
//                .setUsername("john_doe2")
//                .setPassword("password1233")
//                .setEmail("john@example.com2")
//                .setFirstName("John2")
//                .setLastName("Doe2")
//                .setAddress("123 Main Street 2")
//                .setPhoneNumber("0123456780")
//                .build();
//
//        Product newProduct = new Product.Builder()
//                .setProductId(2l)
//                .setName("Anime Hoodie 4")
//                .setPrice(599.99)
//                .build();
//
//        Customer savedCustomer = customerService.create(newCustomer);
//        Product savedProduct = productService.save(newProduct);
//
//        Review review = new Review.Builder().copy(review1)
//                .setProduct(savedProduct)
//                .setUser(savedCustomer)
//                .build();
//
//        Review updated = reviewService.update(review);
//        assertNotNull(updated);
//        System.out.println(updated);
//
//    }
//
//    @Test
//    void delete() {
//        boolean deleted = reviewService.delete(1L);
//        assertTrue(deleted);
//    }
//
//    @Test
//    void getAll() {
//        List<Review> reviews = reviewService.getAll();
//        System.out.println(reviews);
//    }
//}