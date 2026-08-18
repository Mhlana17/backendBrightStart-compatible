//package za.ac.cput.controller;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import za.ac.cput.domain.Customer;
//import za.ac.cput.domain.Delivery;
//import za.ac.cput.domain.Product;
//import za.ac.cput.domain.Review;
//import za.ac.cput.factory.ReviewFactory;
//import za.ac.cput.service.CustomerService;
//import za.ac.cput.service.impl.ProductServiceImpl;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class ReviewControllerTest {
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//    @Autowired
//    private CustomerService customerService;
//    @Autowired
//    private ProductServiceImpl productService;
//
//    private static final String BASE_URL = "http://localhost:8080/review";
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
//                .setProductId(1L)
//                .setName("Anime Hoodie")
//                .setPrice(599.99)
//                .build();
//
//        Customer savedCustomer = customerService.create(customer1);
//        Product savedProduct = productService.save(product1);
//
//        review1 = ReviewFactory.createReview(
//                5, "Excellent quality and design!", savedProduct, savedCustomer);
//
//    }
//
//
//    @Test
//    void create() {
//        String url = BASE_URL + "/create";
//        ResponseEntity<Review> postResponse = restTemplate.postForEntity(url, review1,Review.class);
//        assertNotNull(postResponse);
//        assertNotNull(postResponse.getBody());
//        System.out.println("Created: " + postResponse.getBody());
//    }
//
//    @Test
//    void read() {
//        String url = BASE_URL + "/read/" + 1;
//        ResponseEntity<Review> response = restTemplate.getForEntity(url, Review.class);
//        assertNotNull(response.getBody());
//        System.out.println("Read: " + response.getBody());
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
//                .setProductId(2L)
//                .setName("Anime Hoodie 4")
//                .setPrice(599.99)
//                .build();
//
//        Customer savedCustomer = customerService.create(newCustomer);
//        Product savedProduct = productService.save(newProduct);
//
//        String url = BASE_URL + "/update";
//        Review newReview = new Review.Builder().copy(review1)
//                .setProduct(savedProduct)
//                .setUser(savedCustomer)
//                .build();
//        ResponseEntity<Review> postResponse = restTemplate.postForEntity(url, newReview, Review.class);
//        assertNotNull(postResponse);
//        assertNotNull(postResponse.getBody());
//        System.out.println(postResponse.getBody());
//    }
//
//    @Test
//    void delete() {
//        String url = BASE_URL + "/delete/" + 2;
//        restTemplate.delete(url);
//    }
//
//    @Test
//    void getAll() {
//        String url = BASE_URL + "/getAll";
//        HttpHeaders headers = new HttpHeaders();
//        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
//        System.out.println("All Reviews:");
//        System.out.println(response.getBody());
//    }
//}