package za.ac.cput.service;

import org.springframework.web.multipart.MultipartFile;
import za.ac.cput.domain.Product;
import java.io.IOException;
import java.util.List;

public interface ProductService {
    Product create(Product product);
    Product saveProductWithImage(Product product, MultipartFile imageFile) throws IOException;
    Product update(Product product);
    Product getById(Long id);
    List<Product> getAll();
    List<Product> getAllAvailableProducts(); // NEW: Only products with stock > 0
    List<Product> getOutOfStockProducts();   // NEW: Products with 0 stock
    boolean delete(Long id);

    // NEW: Stock management methods
    Product updateStock(Long productId, int newStock);
    Product decreaseStock(Long productId, int quantity);
    Product increaseStock(Long productId, int quantity);
    boolean isProductAvailable(Long productId, int quantity);
    int getAvailableStock(Long productId);
    List<Product> getLowStockProducts(int threshold);
}