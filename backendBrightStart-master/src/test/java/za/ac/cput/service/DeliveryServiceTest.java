package za.ac.cput.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.CustomerOrder;
import za.ac.cput.domain.Delivery;
import za.ac.cput.factory.DeliveryFactory;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeliveryServiceTest {
    @Autowired
    private DeliveryService deliveryService;

    private Delivery delivery;
    private Delivery delivery2;

    @BeforeEach
    void setUp() {
        delivery = DeliveryFactory.createDelivery(
                1, "Advanced", "123", LocalDate.now().plusDays(5), LocalDate.now().plusDays(10), "Awaiting delivery");

        delivery2 = DeliveryFactory.createDelivery(
                2, "Normal", "456", LocalDate.now().plusDays(10), LocalDate.now().plusDays(20), "Awaiting delivery");

    }

    @Test
    void create() {
        Delivery created = deliveryService.create(delivery);
        assertNotNull(created);
        System.out.println(created);

        Delivery created2 = deliveryService.create(delivery2);
        assertNotNull(created2);
        System.out.println(created2);
    }

    @Test
    void read() {
        Delivery read = deliveryService.read(1);
        assertNotNull(read);
        System.out.println(read);
    }

    @Test
    void update() {
        Delivery delivery1 = new Delivery.Builder().copy(delivery).setDeliveryStatus("Delivered")
                .build();
        Delivery updated = deliveryService.update(delivery1);
        assertNotNull(updated);
        System.out.println(updated);
    }

    @Test
    void delete() {
        boolean deleted = deliveryService.delete(1);
        assertTrue(deleted);
    }

    @Test
    void getAll() {
        List<Delivery> all = deliveryService.getAll();
        System.out.println(all);
    }
}
