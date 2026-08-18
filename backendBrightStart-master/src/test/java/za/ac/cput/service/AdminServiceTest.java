package za.ac.cput.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.Admin;
import za.ac.cput.domain.Customer;
import za.ac.cput.factory.AdminFactory;
import za.ac.cput.factory.CustomerFactory;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminServiceTest {

    @Autowired
    private AdminService service;

    private Admin admin,admin2;

    @BeforeEach
    void setUp() {
        admin = AdminFactory.createAdmin(
                "John_Doe", "John",  "veronicapuleng91@animestore.co.za");
        admin2 = AdminFactory.createAdmin("nosi1","naidoo_1@dev","nosi@animestore.co.za");

    }

    @Test
    void a_create() {
        Admin created = service.create(admin2);
        assertNotNull(created);
        System.out.println(created);

    }

    @Test
    void b_read() {
        Admin read = service.read(21L);
        assertNotNull(read);
        System.out.println(read);
    }

    @Test
    void d_update() {
        Admin admin1 = new Admin.Builder().copy(this.admin).setEmail("liks@animestore.co.za").build();
        Admin updated = service.update(admin1);
        assertNotNull(updated);
        System.out.println(updated);
    }


    @Test
    void getAll() {
        System.out.println(service.getAll());
    }
}
