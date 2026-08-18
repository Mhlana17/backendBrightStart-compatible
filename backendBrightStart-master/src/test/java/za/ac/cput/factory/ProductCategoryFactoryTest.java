package za.ac.cput.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.ProductCategory;

import static org.junit.jupiter.api.Assertions.*;

class ProductCategoryFactoryTest {
    @Test
    void testCreateProductCategory() {
        ProductCategory category = ProductCategoryFactory.createProductCategory(1l, "Books");
        assertNotNull(category);
        assertEquals("C001", category.getCategoryId());
        assertEquals("Books", category.getCategoryName());
    }
}
