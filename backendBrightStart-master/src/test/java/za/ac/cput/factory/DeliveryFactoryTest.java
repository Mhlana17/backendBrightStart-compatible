package za.ac.cput.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Delivery;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DeliveryFactoryTest {
    private Delivery delivery;
    @BeforeEach
    void setUp() {
        delivery = DeliveryFactory.createDelivery(
                1, "Advanced", "123", LocalDate.now().plusDays(5), LocalDate.now().plusDays(10), "Awaiting delivery");

    }

    @Test
    void createAdmin() {
        assertNotNull(delivery);
        System.out.println(delivery);
    }
}