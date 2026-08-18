package za.ac.cput.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.CustomerOrder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerOrderServiceTest {

    @Autowired
    private CustomerOrderService orderService;

    @Test
    void testBasicServiceOperations() {
        System.out.println("Testing basic service operations...");

        try {
            // Test 1: Service injection
            assertNotNull(orderService, "Service should be injected");
            System.out.println("Service injection works");

            // Test 2: GetAll should return empty list (fresh database)
            List<CustomerOrder> orders = orderService.getAll();
            assertNotNull(orders, "getAll() should never return null");
            System.out.println("getAll() works - returned list with " + orders.size() + " items");

            // Test 3: Read non-existent order should return null
            CustomerOrder nonExistent = orderService.read(999L);
            assertNull(nonExistent, "Reading non-existent order should return null");
            System.out.println("read() works for non-existent orders");

            System.out.println("All basic operations work!");

        } catch (Exception e) {
            System.out.println("Test failed with exception: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed: " + e.getMessage());
        }
    }
}