package za.ac.cput.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Admin;
import za.ac.cput.domain.Customer;

import static org.junit.jupiter.api.Assertions.*;

class AdminFactoryTest {

    private Admin admin,admin2;
    @BeforeEach
    void setUp() {
        admin = AdminFactory.createAdmin(
                "John_Doe", "naidoo_1@dev", "veronicapuleng91@gmail.com");
        admin2 = AdminFactory.createAdmin("nosi1","naidoo_1@dev","nosi@animestore.co.za");


    }

    @Test
    void createAdmin() {
        assertNotNull(admin2);
        System.out.println(admin2);
    }
}
