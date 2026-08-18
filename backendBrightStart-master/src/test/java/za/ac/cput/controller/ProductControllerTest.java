package za.ac.cput.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import za.ac.cput.service.impl.ProductServiceImpl;
import za.ac.cput.domain.Product;
import za.ac.cput.domain.ProductCategory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8080/product";

    @Test
    void testCreateAndGetProduct() {
        ProductCategory category = new ProductCategory.Builder()
                .setCategoryId(1l)
                .setCategoryName("Anime T-Shirts")
                .build();

        Product product = new Product.Builder()
                .setProductId(1L)
                .setName("Itachi Uchiha T-Shirt")
                .setPrice(199.99)
                .build();
        Product product2 = new Product.Builder()
                .setProductId(2L)
                .setName("Demon Slayer Mug")
                .setPrice(59.99)
                .build();

        String url = BASE_URL + "/save";
        ResponseEntity<Product> postResponse = restTemplate.postForEntity(url, product2,Product.class);
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
        System.out.println("Created: " + postResponse.getBody());

        ResponseEntity<Product> postResponse2 = restTemplate.postForEntity(url, product,Product.class);
        assertNotNull(postResponse2);
        assertNotNull(postResponse2.getBody());
        System.out.println("Created: " + postResponse2.getBody());
    }

    @Test
    void testGetById() {
        String url = BASE_URL + "/read/" + "PROD006";
        ResponseEntity<Product> response = restTemplate.getForEntity(url, Product.class);
        assertNotNull(response.getBody());
        System.out.println("Read: " + response.getBody());
    }

    @Test
    void testDelete() {
        String url = BASE_URL + "/delete/" + "PROD006";
        restTemplate.delete(url);
    }
}
