package za.ac.cput.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.Customer;
import za.ac.cput.factory.CustomerFactory;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

class CustomerServiceTest {

    @Autowired
    private CustomerService service;

    private Customer customer = CustomerFactory.createCustomer(
            "john_doe", "John", "password123", "veronicapuleng91@gmail.com", "123 Main St", "555-1234");

    @Test
    void a_create() {
        Customer created = service.create(customer);
        assertNotNull(created);
        System.out.println(created);

    }

    @Test
    void b_read() {
        Customer read = service.read(1L);
        assertNotNull(read);
        System.out.println(read);
    }

    @Test
    void d_update() {
        Customer customer = new Customer.Builder().copy(this.customer).setUsername("john_doe2")
                .build();
        Customer updated = service.update(customer);
        assertNotNull(updated);
        System.out.println(updated);
    }


    @Test
    void getAll() {
        System.out.println(service.getAll());
    }
}
