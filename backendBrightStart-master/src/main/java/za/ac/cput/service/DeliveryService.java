package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Delivery;
import za.ac.cput.repository.DeliveryRepository;

import java.util.List;

@Service
public class DeliveryService implements IDeliveryService{
    @Autowired
    private DeliveryRepository deliveryRepository;

    @Override
    public Delivery create(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    @Override
    public Delivery read(Integer integer) {
        return deliveryRepository.findById(integer).orElse(null);
    }

    @Override
    public Delivery update(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    @Override
    public boolean delete(Integer integer) {
        deliveryRepository.deleteById(integer);
        return !deliveryRepository.existsById(integer);
    }

    @Override
    public List<Delivery> getAll() {
        return deliveryRepository.findAll();
    }
}