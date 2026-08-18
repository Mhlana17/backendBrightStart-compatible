package za.ac.cput.factory;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Inventory;
import za.ac.cput.domain.Product;
import za.ac.cput.domain.ProductStatus;

import static org.junit.jupiter.api.Assertions.*;

class InventoryFactoryTest {

    // Example product image
    byte[] sampleImage = "fakeImageData".getBytes();

    // Create product using updated ProductFactory
    Product product = ProductFactory.createProduct(
            "Manga Book",
            199.99,
            50,
            sampleImage
    );

    public Inventory inventory1 = InventoryFactory.createInventory(product, ProductStatus.FEW_IN_STOCK);
    public Inventory inventory2 = InventoryFactory.createInventory(product, ProductStatus.FEW_IN_STOCK);
    public Inventory inventory3 = InventoryFactory.createInventory(product, ProductStatus.OUT_OF_STOCK); // Invalid test

    @Test
    @Order(1)
    public void testCreateInventory() {
        assertNotNull(inventory1);
        System.out.println(inventory1.toString());
    }

    @Test
    @Order(2)
    public void testCreateInventoryWithAllAttributes() {
        assertNotNull(inventory2);
        System.out.println(inventory2.toString());
    }

    @Test
    @Order(3)
    public void testCreateInventoryThatFails() {
        assertNotNull(inventory3);
        System.out.println(inventory3);
    }

    @Test
    @Disabled
    @Order(4)
    public void testNotImplemented() {
    }
}
