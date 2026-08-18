package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Inventory;
import za.ac.cput.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/inventory")
//@CrossOrigin(origins = "http://localhost:5173") // ADD THIS LINE (or keep your existing one if you prefer)
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/create")
    public Inventory create(@RequestBody Inventory inventory) {
        return inventoryService.create(inventory);
    }

    @GetMapping("/read/{inventoryId}")
    public Inventory read(@PathVariable Long inventoryId){
        return inventoryService.read(inventoryId);
    }

    @PostMapping("/update")
    public Inventory update(@RequestBody Inventory inventory) {
        return inventoryService.update(inventory);
    }

    @DeleteMapping("/delete/{inventoryId}")
    public boolean delete(@PathVariable Long inventoryId){
        return inventoryService.delete(inventoryId);
    }

    @GetMapping("/getAll")
    public List<Inventory> getAll(){
        return inventoryService.getAll();
    }
}