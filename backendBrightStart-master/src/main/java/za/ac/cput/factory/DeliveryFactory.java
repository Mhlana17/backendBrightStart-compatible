package za.ac.cput.factory;
/*Deliveryfactory.java
Author : N Mshweshwe
date: 17 may 2025
*/


import za.ac.cput.domain.Delivery;

import java.time.LocalDate;

public class DeliveryFactory {

    public static Delivery createDelivery(
            int deliveryId,
            String shippingMethod,
            String trackingNumber,
            LocalDate shippingDate,
            LocalDate estimatedDeliveryDate,
            String deliveryStatus) {

        if (shippingMethod == null || shippingMethod.isEmpty() ||
                trackingNumber == null || trackingNumber.isEmpty() ||
                shippingDate == null || estimatedDeliveryDate == null || deliveryStatus == null) {
            return null;
        }

        return new Delivery.Builder()
                .setDeliveryId(deliveryId)
                .setShippingMethod(shippingMethod)
                .setTrackingNumber(trackingNumber)
                .setShippingDate(shippingDate)
                .setDeliveryDate(estimatedDeliveryDate)
                .setDeliveryStatus(deliveryStatus)
                .build();
    }
}