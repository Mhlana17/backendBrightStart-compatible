package za.ac.cput.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Payment;
import za.ac.cput.domain.PaymentMethod;
import za.ac.cput.domain.PaymentStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentFactoryTest {

    private static LocalDateTime date;

    private Payment payment1;

    @BeforeEach
    void setUp() {
        date = LocalDateTime.now().plusDays(21);
        payment1 = PaymentFactory.createPayment( 499.99, PaymentMethod.CREDIT_CARD,
                PaymentStatus.COMPLETED, "TXN-298374","1234567812345678",
                date);
    }

    @Test
    void createPayment() {

        assertNotNull(payment1);
        System.out.println(payment1.toString());
    }
}