package za.ac.cput.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.domain.Payment;
import za.ac.cput.domain.PaymentMethod;
import za.ac.cput.domain.PaymentStatus;
import za.ac.cput.factory.PaymentFactory;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;




@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PaymentControllerTest {

    private static Payment payment;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8080/payment";

    @BeforeAll
    public static void setUp() {
        LocalDateTime date = LocalDateTime.now().plusDays(21);
        payment = PaymentFactory.createPayment( 999.99, PaymentMethod.CREDIT_CARD,
                PaymentStatus.COMPLETED, "TXN-0001",
                "1234567812345678", date);
    }

    @Test
    void a_create() {
        String url = BASE_URL + "/create";
        ResponseEntity<Payment> postResponse = restTemplate.postForEntity(url, payment, Payment.class);
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
        System.out.println("Created: " + postResponse.getBody());
    }

    @Test
    void b_read() {
        String url = BASE_URL + "/read/" + 2;
        ResponseEntity<Payment> response = restTemplate.getForEntity(url, Payment.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        System.out.println("Read: " + response.getBody());
    }

    @Test
    void c_update() {
        Payment updatedPayment = new Payment.Builder()
                .copy(payment)
                .setAmount(1199.99)
                .build();

        String url = BASE_URL + "/update";

        ResponseEntity<Payment> postResponse = restTemplate.postForEntity(url, updatedPayment, Payment.class);

        assertNotNull(postResponse.getBody());
        System.out.println("Updated: " + postResponse.getBody());
    }

    @Test
    void d_delete() {
        String url = BASE_URL + "/delete/" + 5;
        restTemplate.delete(url);
        System.out.println("Deleted: true");
    }

    @Test
    void e_getAll() {
        String url = BASE_URL + "/getAll";
        ResponseEntity<Payment[]> response = restTemplate.getForEntity(url, Payment[].class);
        assertNotNull(response.getBody());
        System.out.println("All Payments:");
        for (Payment p : response.getBody()) {
            System.out.println(p);

        }
    }
}