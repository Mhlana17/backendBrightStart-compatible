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
import za.ac.cput.domain.Inventory;
import za.ac.cput.domain.Product;
import za.ac.cput.domain.ProductStatus;
import za.ac.cput.factory.InventoryFactory;
import za.ac.cput.factory.ProductFactory;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private Inventory inventory1;
    private Inventory inventory2;
    private static final String BASE_URL = "http://localhost:8080/inventory";

    @BeforeEach
    void setUp() {
        // Fake image data for testing
        byte[] sampleImage1 = "fakeImageData1".getBytes();
        byte[] sampleImage2 = "fakeImageData2".getBytes();

        Product product1 = ProductFactory.createProduct(
                "Manga Book",
                199.99,
                50,
                sampleImage1
        );

        Product product2 = ProductFactory.createProduct(
                "Manga Book 10",
                299.99,
                30,
                sampleImage2
        );

        inventory1 = InventoryFactory.createInventory(product1, ProductStatus.FEW_IN_STOCK);
        inventory2 = InventoryFactory.createInventory(product2, ProductStatus.FEW_IN_STOCK);
    }

    @Test
    void create() {
        String url = BASE_URL + "/create";
        ResponseEntity<Inventory> postResponse = restTemplate.postForEntity(url, inventory1, Inventory.class);
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
        System.out.println("Created Inventory 1: " + postResponse.getBody());

        ResponseEntity<Inventory> postResponse2 = restTemplate.postForEntity(url, inventory2, Inventory.class);
        assertNotNull(postResponse2);
        assertNotNull(postResponse2.getBody());
        System.out.println("Created Inventory 2: " + postResponse2.getBody());
    }

    @Test
    void read() {
        String url = BASE_URL + "/read/" + 22;
        ResponseEntity<Inventory> response = restTemplate.getForEntity(url, Inventory.class);
        assertNotNull(response.getBody());
        System.out.println("Read Inventory: " + response.getBody());
    }

    @Test
    void update() {
        String url = BASE_URL + "/update";

        byte[] updatedImage = "updatedImageData".getBytes();
        Product updatedProduct = ProductFactory.createProduct(
                "Manga Book 91",
                219.99,
                45,
                updatedImage
        );

        Inventory newInventory = new Inventory.Builder()
                .copy(inventory1)
                .setProduct(updatedProduct)
                .setStatus(ProductStatus.OUT_OF_STOCK)
                .build();

        ResponseEntity<Inventory> postResponse = restTemplate.postForEntity(url, newInventory, Inventory.class);
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
        System.out.println("Updated Inventory: " + postResponse.getBody());
    }

    @Test
    void delete() {
        String url = BASE_URL + "/delete/" + 2;
        restTemplate.delete(url);
        System.out.println("Deleted Inventory with ID 2");
    }

    @Test
    void getAll() {
        String url = BASE_URL + "/getAll";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        assertNotNull(response);
        System.out.println("All Inventories: " + response.getBody());
    }
}
