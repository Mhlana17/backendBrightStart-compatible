package za.ac.cput.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.service.impl.ProductCategoryServiceImpl;
import za.ac.cput.domain.ProductCategory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductCategoryServiceTest {

    @Autowired
    private ProductCategoryServiceImpl categoryService;

    @Test
    void testSaveCategory() {
        ProductCategory category = new ProductCategory.Builder()
                .setCategoryId(1l)
                .setCategoryName("Anime Figures")
                .build();

        ProductCategory saved = categoryService.save(category);
        assertNotNull(saved);
        assertEquals("Anime Figures", saved.getCategoryName());
    }

    @Test
    void testGetAllCategories() {
        ProductCategory category1 = new ProductCategory.Builder()
                .setCategoryId(2l)
                .setCategoryName("Anime Posters")
                .build();

        ProductCategory category2 = new ProductCategory.Builder()
                .setCategoryId(3l)
                .setCategoryName("Anime Hoodies")
                .build();

        categoryService.save(category1);
        categoryService.save(category2);

        List<ProductCategory> all = categoryService.getAll();
        System.out.println("All: " + all);
    }

    @Test
    void testDeleteCategory() {
        ProductCategory category = new ProductCategory.Builder()
                .setCategoryId(4l)
                .setCategoryName("Anime Keychains")
                .build();

        categoryService.save(category);
        assertFalse(categoryService.getAll().isEmpty());

        boolean isDeleted = categoryService.delete(4l);
        assertTrue(isDeleted);
    }
}
