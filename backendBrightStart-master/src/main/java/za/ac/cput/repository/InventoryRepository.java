package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
