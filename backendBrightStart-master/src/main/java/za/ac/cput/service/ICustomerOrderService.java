package za.ac.cput.service;

import za.ac.cput.domain.CustomerOrder;
import java.util.List;

public interface ICustomerOrderService extends IService<CustomerOrder, Long> {
    List<CustomerOrder> getOrdersByCustomerId(Long userId);
    CustomerOrder getOrderByOrderNumber(String orderNumber);
}