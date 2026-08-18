package za.ac.cput.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Product;

import static org.junit.jupiter.api.Assertions.*;

class ProductFactoryTest {

    @Test
    void testCreateProduct() {
        // Example image as byte[] (in real tests, you might load from a file or mock it)
        byte[] sampleImage = "fakeImageData".getBytes();

        Product product = ProductFactory.createProduct(

                "Manga Book",
                199.99,
                50,
                sampleImage
        );

        assertNotNull(product);
        assertEquals("Manga Book", product.getName());
        assertEquals(199.99, product.getPrice());
        assertEquals(50, product.getStock());
        assertArrayEquals(sampleImage, product.getProductImage());
    }
}
