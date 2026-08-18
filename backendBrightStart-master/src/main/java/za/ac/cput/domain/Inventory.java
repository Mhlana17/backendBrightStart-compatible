//Inventory POJO class
//Author :S Mzaza(220030898)
//Date: 11 May 2025
package za.ac.cput.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long inventoryId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    public Inventory() {
    }

    private Inventory(Builder builder) {
        this.inventoryId = builder.inventoryId;
        this.product = builder.product;
        this.status = builder.status;
    }

    public Long getInventoryId() {
        return inventoryId;
    }

    public Product getProduct() {
        return product;
    }

    public ProductStatus getStatus() {
        return status;
    }

    // ADDED SETTER METHOD
    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Inventory inventory = (Inventory) o;
        return Objects.equals(inventoryId, inventory.inventoryId) && Objects.equals(product, inventory.product) && status == inventory.status;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "inventoryId=" + inventoryId +
                ", product=" + product +
                ", status=" + status +
                '}';
    }

    public static class Builder {
        private Long inventoryId;
        private Product product;
        private ProductStatus status;

        public Builder setInventoryId(Long inventoryId) {
            this.inventoryId = inventoryId;
            return this;
        }

        public Builder setProduct(Product product) {
            this.product = product;
            return this;
        }

        public Builder setStatus(ProductStatus status) {
            this.status = status;
            return this;
        }

        public Builder copy(Inventory inventory) {
            this.inventoryId = inventory.inventoryId;
            this.product = inventory.product;
            this.status = inventory.status;
            return this;
        }

        public Inventory build() {
            return new Inventory(this);
        }
    }
}