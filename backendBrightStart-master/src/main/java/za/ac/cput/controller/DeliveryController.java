package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Admin;
import za.ac.cput.domain.Delivery;
import za.ac.cput.service.AdminService;
import za.ac.cput.service.DeliveryService;

import java.util.List;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {
    @Autowired
    DeliveryService deliveryService;

    @PostMapping("/create")
    public Delivery create(@RequestBody Delivery delivery) {
        return deliveryService.create(delivery);
    }

    @GetMapping("/read/{deliveryId}")
    public Delivery read(@PathVariable int deliveryId){
        return deliveryService.read(deliveryId);
    }

    @PostMapping("/update")
    public Delivery update(@RequestBody Delivery delivery) {
        return deliveryService.update(delivery);
    }

    @DeleteMapping("/delete/{deliveryId}")
    public void delete(@PathVariable int deliveryId){
        deliveryService.delete(deliveryId);
    }

    @GetMapping("/getAll")
    public List<Delivery> getAll(){
        return deliveryService.getAll();
    }
}
