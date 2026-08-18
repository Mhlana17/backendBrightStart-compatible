package za.ac.cput.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import za.ac.cput.domain.Anime;
import za.ac.cput.service.impl.ProductCategoryServiceImpl;
import za.ac.cput.domain.ProductCategory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductCategoryControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8080/category";

    @Test
    void testCreateCategory() {
        ProductCategory category = new ProductCategory.Builder()
                .setCategoryId(1l)
                .setCategoryName("Anime Stickers")
                .build();

        ProductCategory category2 = new ProductCategory.Builder()
                .setCategoryId(2l)
                .setCategoryName("Anime Caps")
                .build();

        String url = BASE_URL + "/save";
        ResponseEntity<ProductCategory> postResponse = restTemplate.postForEntity(url, category,ProductCategory.class);
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
        System.out.println("Created: " + postResponse.getBody());

        ResponseEntity<ProductCategory> postResponse2 = restTemplate.postForEntity(url, category2,ProductCategory.class);
        assertNotNull(postResponse2);
        assertNotNull(postResponse2.getBody());
        System.out.println("Created: " + postResponse2.getBody());
    }

    @Test
    void testGetById() {
        String url = BASE_URL + "/read/" + "CAT005";
        ResponseEntity<ProductCategory> response = restTemplate.getForEntity(url, ProductCategory.class);
        assertNotNull(response.getBody());
        System.out.println("Read: " + response.getBody());
    }

    @Test
    void testDeleteCategory() {
        String url = BASE_URL + "/delete/" + "CAT007";
        restTemplate.delete(url);
    }
}
