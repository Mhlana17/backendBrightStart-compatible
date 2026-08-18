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
import za.ac.cput.domain.Customer;
import za.ac.cput.domain.Product;
import za.ac.cput.factory.CustomerFactory;


import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private Customer customer;
    private  Customer customer2;
    private static final String BASE_URL = "http://localhost:8080/customer";


    @BeforeEach
    void setUp() {
        customer = CustomerFactory.createCustomer(
                "john_doe", "John", "password123", "veronicapuleng91@gmail.com", "123 Main St", "555-1234");
        customer2 = CustomerFactory.createCustomer(
                "john_doe2", "John2", "password1233", "veronicapuleng91@gmail.com2", "123 Main St2", "555-12342");
    }

    @Test
    void create() {
        String url = BASE_URL + "/create";
        ResponseEntity<Customer> postResponse = restTemplate.postForEntity(url, customer,Customer.class);
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
        System.out.println("Created: " + postResponse.getBody());

        ResponseEntity<Customer> postResponse2 = restTemplate.postForEntity(url, customer2,Customer.class);
        assertNotNull(postResponse2);
        assertNotNull(postResponse2.getBody());
        System.out.println("Created: " + postResponse2.getBody());
    }

    @Test
    void read() {
        String url = BASE_URL + "/read/" + 1L;
        ResponseEntity<Customer> response = restTemplate.getForEntity(url, Customer.class);
        assertNotNull(response.getBody());
        System.out.println("Read: " + response.getBody());
    }

    @Test
    void update() {
        String url = BASE_URL + "/update";
        Customer newCustomer = new Customer.Builder().copy(customer).setPassword("password456").build();
        ResponseEntity<Customer> postResponse = restTemplate.postForEntity(url, newCustomer, Customer.class);
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
        System.out.println(postResponse.getBody());
    }

    @Test
    void delete() {
        String url = BASE_URL + "/delete/" + 17;
        restTemplate.delete(url);
    }

    @Test
    void getAll() {
        String url = BASE_URL + "/getAll";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        System.out.println("All Landlords:");
        System.out.println(response.getBody());
    }
}
