package za.ac.cput.factory;

import za.ac.cput.domain.Product;

public class ProductFactory {
    public static Product createProduct( String name, double price, int stock, byte[] productImage) {
        return new Product.Builder()

                .setName(name)
                .setPrice(price)
                .setStock(stock)
                .setProductImage(productImage)
                .build();
    }
}
