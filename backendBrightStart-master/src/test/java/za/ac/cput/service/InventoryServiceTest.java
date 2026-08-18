package za.ac.cput.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.Inventory;
import za.ac.cput.domain.Product;
import za.ac.cput.domain.ProductStatus;
import za.ac.cput.factory.InventoryFactory;
import za.ac.cput.factory.ProductFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InventoryServiceTest {

    @Autowired
    private InventoryService inventoryService;

    private Inventory inventory1;
    private Inventory inventory2;

    @BeforeEach
    void setUp() {
        // Sample image data
        byte[] sampleImage1 = "fakeImageData1".getBytes();
        byte[] sampleImage2 = "fakeImageData2".getBytes();

        // Updated ProductFactory usage with productImage
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
        Inventory created = inventoryService.create(inventory1);
        assertNotNull(created);
        System.out.println("Created Inventory 1: " + created);

        Inventory created2 = inventoryService.create(inventory2);
        assertNotNull(created2);
        System.out.println("Created Inventory 2: " + created2);
    }

    @Test
    void read() {
        Inventory read = inventoryService.read(2L);
        assertNotNull(read);
        System.out.println("Read Inventory: " + read);
    }

    @Test
    void update() {
        Inventory inventory = new Inventory.Builder()
                .copy(inventory1)
                .setStatus(ProductStatus.OUT_OF_STOCK)
                .build();

        Inventory updated = inventoryService.update(inventory);
        assertNotNull(updated);
        System.out.println("Updated Inventory: " + updated);
    }

    @Test
    void delete() {
        boolean deleted = inventoryService.delete(1L);
        assertTrue(deleted);
        System.out.println("Deleted Inventory with ID 1: " + deleted);
    }

    @Test
    void getAll() {
        List<Inventory> all = inventoryService.getAll();
        assertNotNull(all);
        System.out.println("All Inventories: " + all);
    }
}
