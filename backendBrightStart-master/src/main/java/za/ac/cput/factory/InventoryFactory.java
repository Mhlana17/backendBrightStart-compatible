package za.ac.cput.factory;

import za.ac.cput.domain.Inventory;
import za.ac.cput.domain.Product;
import za.ac.cput.domain.ProductStatus;
import za.ac.cput.util.Helper;

public class InventoryFactory {

    public static Inventory createInventory(Product product, ProductStatus status) {
        if (product == null || status == null) {
            return null;
        }

        // If you need to generate inventoryId, you can use Helper if available
        // Long inventoryId = Helper.generateId(); // Uncomment if you have this method

        return new Inventory.Builder()
                // .setInventoryId(inventoryId) // Uncomment if generating ID
                .setProduct(product)
                .setStatus(status)
                .build();
    }

    // Overloaded method that calculates status based on stock
    public static Inventory createInventoryWithAutoStatus(Product product) {
        if (product == null) {
            return null;
        }

        // Calculate status based on product stock
        ProductStatus status = calculateStatus(product.getStock());

        return new Inventory.Builder()
                .setProduct(product)
                .setStatus(status)
                .build();
    }

    // Method to create inventory with product and stock (auto-calculates status)
    public static Inventory createInventory(Product product, int stock) {
        if (product == null) {
            return null;
        }

        ProductStatus status = calculateStatus(stock);

        return new Inventory.Builder()
                .setProduct(product)
                .setStatus(status)
                .build();
    }

    private static ProductStatus calculateStatus(int stock) {
        if (stock <= 0) {
            return ProductStatus.OUT_OF_STOCK;
        } else if (stock <= 10) {
            return ProductStatus.FEW_IN_STOCK;
        } else {
            return ProductStatus.IN_STOCK;
        }
    }
}