package za.ac.cput.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import za.ac.cput.domain.Admin;
import za.ac.cput.domain.CustomerOrder;
import za.ac.cput.domain.Delivery;
import za.ac.cput.factory.DeliveryFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeliveryControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private Delivery delivery;
    private Delivery delivery2;
    private static final String BASE_URL = "http://localhost:8080/delivery";

    @BeforeEach
    void setUp() {
        delivery = DeliveryFactory.createDelivery(
                1, "Advanced", "1233", LocalDate.now().plusDays(5), LocalDate.now().plusDays(10), "Awaiting delivery");

        delivery2 = DeliveryFactory.createDelivery(
                2, "Normal", "4566", LocalDate.now().plusDays(10), LocalDate.now().plusDays(20), "Awaiting delivery");

    }

    @Test
    void create() {
        String url = BASE_URL + "/create";
        ResponseEntity<Delivery> postResponse = restTemplate.postForEntity(url, delivery,Delivery.class);
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
        System.out.println("Created: " + postResponse.getBody());

        ResponseEntity<Delivery> postResponse2 = restTemplate.postForEntity(url, delivery2,Delivery.class);
        assertNotNull(postResponse2);
        assertNotNull(postResponse2.getBody());
        System.out.println("Created: " + postResponse2.getBody());
    }

    @Test
    void read() {
        String url = BASE_URL + "/read/" + 1;
        ResponseEntity<Delivery> response = restTemplate.getForEntity(url, Delivery.class);
        assertNotNull(response.getBody());
        System.out.println("Read: " + response.getBody());
    }

    @Test
    void update() {
        String url = BASE_URL + "/update";
        Delivery newDelivery = new Delivery.Builder().copy(delivery).setDeliveryDate(LocalDate.now().plusDays(3)).build();
        ResponseEntity<Delivery> postResponse = restTemplate.postForEntity(url, newDelivery, Delivery.class);
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
        System.out.println(postResponse.getBody());
    }

    @Test
    void delete() {
        String url = BASE_URL + "/delete/" + 2;
        restTemplate.delete(url);
    }

    @Test
    void getAll() {
        String url = BASE_URL + "/getAll";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        System.out.println("All Deliveries:");
        System.out.println(response.getBody());
    }
}