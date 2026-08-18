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
import za.ac.cput.domain.Customer;
import za.ac.cput.domain.Product;
import za.ac.cput.factory.AdminFactory;
import za.ac.cput.factory.CustomerFactory;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private Admin admin, admin2;
    private static final String BASE_URL = "http://localhost:8080/admin";


    @BeforeEach
    void setUp() {
        admin = AdminFactory.createAdmin(
                "veronica@animestore.co.za",  "naidoo_1@dev", "veronica@animestore.co.za");
        admin2 = AdminFactory.createAdmin("Nos","naidoo_1@dev","nosi@animestore.co.za");
    }

    @Test
    void create() {
        String url = BASE_URL + "/create";
//        ResponseEntity<Admin> postResponse = restTemplate.postForEntity(url, admin,Admin.class);
//        assertNotNull(postResponse);
//        assertNotNull(postResponse.getBody());
//        System.out.println("Created: " + postResponse.getBody());

        ResponseEntity<Admin> postResponse2 = restTemplate.postForEntity(url, admin2,Admin.class);
        assertNotNull(postResponse2);
        assertNotNull(postResponse2.getBody());
        System.out.println("Created: " + postResponse2.getBody());
    }

    @Test
    void read() {
        String url = BASE_URL + "/read/" + 25L;
        ResponseEntity<Admin> response = restTemplate.getForEntity(url, Admin.class);
        assertNotNull(response.getBody());
        System.out.println("Read: " + response.getBody());
    }

    @Test
    void update() {
        String url = BASE_URL + "/update";
        Admin newAdmin = new Admin.Builder().copy(admin).setPassword("password456").build();
        ResponseEntity<Admin> postResponse = restTemplate.postForEntity(url, newAdmin, Admin.class);
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
        System.out.println(postResponse.getBody());
    }

    @Test
    void delete() {
        String url = BASE_URL + "/delete/" + 26L;
        restTemplate.delete(url);
    }

    @Test
    void getAll() {
        String url = BASE_URL + "/getAll";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        System.out.println("All Admins:");
        System.out.println(response.getBody());
    }
}
