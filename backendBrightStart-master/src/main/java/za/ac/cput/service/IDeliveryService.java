package za.ac.cput.service;

import za.ac.cput.domain.Delivery;

import java.util.List;

public interface IDeliveryService extends IService<Delivery, Integer> {
    List<Delivery> getAll();
}