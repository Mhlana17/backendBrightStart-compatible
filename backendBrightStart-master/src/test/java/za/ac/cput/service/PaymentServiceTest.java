package za.ac.cput.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Payment;
import za.ac.cput.domain.PaymentMethod;
import za.ac.cput.domain.PaymentStatus;
import za.ac.cput.factory.PaymentFactory;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Service
class PaymentServiceTest {

    @Autowired
    private IPaymentService service;

    private static Payment payment;

    @BeforeAll
    static void setUp() {
        LocalDateTime date = LocalDateTime.now().plusDays(21);


        payment = PaymentFactory.createPayment(499.99, PaymentMethod.CREDIT_CARD,
                PaymentStatus.COMPLETED, "TXN-298374", "1234567812345678",
                date);
    }

    @Test
    @Order(1)
    void a_create() {
        Payment created = service.create(payment);


        assertEquals(499.99, created.getAmount());
        assertEquals(PaymentStatus.COMPLETED, created.getStatus());
        assertEquals(PaymentMethod.CREDIT_CARD, created.getMethod());

        payment = created;
        System.out.println("Created: " + created);
    }

    @Test
    @Order(2)
    void b_read() {

        Payment read = service.read(payment.getPaymentId());
        assertNotNull(read);
        assertEquals(payment.getPaymentId(), read.getPaymentId());
        System.out.println("Read: " + read);
    }

    @Test
    @Order(3)
    void c_update() {
        Payment updatedPayment = new Payment.Builder()
                .copy(payment)
                .setAmount(1399.99)
                .build();

        Payment updated = service.update(updatedPayment);
        assertNotNull(updated);
        assertEquals(1399.99, updated.getAmount());
        payment = updated;
        System.out.println("Updated: " + updated);
    }

    @Test
    @Order(4)
    void d_delete() {
        boolean deleted = service.delete(payment.getPaymentId());
        assertTrue(deleted);
        System.out.println("Deleted: " + deleted);
    }

    @Test
    @Order(5)
    void e_getAll() {
        System.out.println("All Payments: " + service.getAll());
    }
}