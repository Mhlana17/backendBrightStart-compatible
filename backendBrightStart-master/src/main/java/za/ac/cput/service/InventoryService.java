package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Inventory;
import za.ac.cput.domain.Product;
import za.ac.cput.domain.ProductStatus;
import za.ac.cput.factory.InventoryFactory;
import za.ac.cput.repository.InventoryRepository;
import za.ac.cput.service.ProductService;

import java.util.List;

@Service
public class InventoryService implements IInventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductService productService;

    @Override
    public Inventory create(Inventory inventory) {
        // Get the full product with stock information to calculate status
        if (inventory.getProduct() != null && inventory.getProduct().getProductId() != null) {
            Product fullProduct = productService.getById(inventory.getProduct().getProductId());
            if (fullProduct != null) {
                // Calculate status based on the actual product stock
                ProductStatus status = calculateStatus(fullProduct.getStock());

                // Create new inventory with the full product and calculated status
                Inventory newInventory = InventoryFactory.createInventory(fullProduct, status);
                return inventoryRepository.save(newInventory);
            }
        }

        // If no product found, save with OUT_OF_STOCK
        inventory.setStatus(ProductStatus.OUT_OF_STOCK);
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory read(Long inventoryId) {
        return inventoryRepository.findById(inventoryId).orElse(null);
    }

    @Override
    public Inventory update(Inventory inventory) {
        if (inventoryRepository.existsById(inventory.getInventoryId())) {
            // Recalculate status based on current product stock
            if (inventory.getProduct() != null && inventory.getProduct().getProductId() != null) {
                Product fullProduct = productService.getById(inventory.getProduct().getProductId());
                if (fullProduct != null) {
                    ProductStatus status = calculateStatus(fullProduct.getStock());

                    // Use builder to create updated inventory
                    Inventory updatedInventory = new Inventory.Builder()
                            .setInventoryId(inventory.getInventoryId())
                            .setProduct(fullProduct)
                            .setStatus(status)
                            .build();

                    return inventoryRepository.save(updatedInventory);
                }
            }
            return inventoryRepository.save(inventory);
        }
        return null;
    }

    @Override
    public boolean delete(Long inventoryId) {
        if (inventoryRepository.existsById(inventoryId)) {
            inventoryRepository.deleteById(inventoryId);
            return true;
        }
        return false;
    }

    @Override
    public List<Inventory> getAll() {
        return inventoryRepository.findAll();
    }

    private ProductStatus calculateStatus(int stock) {
        if (stock <= 0) {
            return ProductStatus.OUT_OF_STOCK;
        } else if (stock <= 10) {
            return ProductStatus.FEW_IN_STOCK;
        } else {
            return ProductStatus.IN_STOCK;
        }
    }
}