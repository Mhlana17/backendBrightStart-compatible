package za.ac.cput.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Delivery {
    @Id
    private int deliveryId;
    private String shippingMethod;
    private String trackingNumber;
    private String deliveryStatus;
    private LocalDate shippingDate;
    private LocalDate deliveryDate;


    protected Delivery(){

    }

    public Delivery(Builder builder) {
        this.deliveryId = builder.deliveryId;
        this.shippingMethod = builder.shippingMethod;
        this.trackingNumber = builder.trackingNumber;
        this.deliveryStatus = builder.deliveryStatus;
        this.shippingDate = builder.shippingDate;
        this.deliveryDate = builder.deliveryDate;


    }


    public int getDeliveryId() {return deliveryId;}
    public String  getShippingMethod() {return shippingMethod;}
    public String  getShippingTrackingNumber() {return trackingNumber;}
    public String  getShippingDeliveryStatus() {return deliveryStatus;}
    public LocalDate  getShippingDate() {return shippingDate;}
    public LocalDate getDeliveryDate() {return deliveryDate;}


    @Override
    public String toString() {
        return "Delivery{" +
                "deliveryId=" + deliveryId +
                ", shippingMethod='" + shippingMethod + '\'' +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", deliveryStatus='" + deliveryStatus + '\'' +
                ", shippingDate=" + shippingDate +
                ", deliveryDate=" + deliveryDate +
                '}';
    }


    public static class Builder {
        private int deliveryId;
        private String shippingMethod;
        private String trackingNumber;
        private String deliveryStatus;
        private LocalDate shippingDate;
        private LocalDate deliveryDate;




        public Builder setDeliveryId(int deliveryId) {
            this.deliveryId = deliveryId;
            return this;
        }
        public Builder setShippingMethod(String shippingMethod) {
            this.shippingMethod = shippingMethod;
            return this;
        }
        public Builder setTrackingNumber(String trackingNumber) {
            this.trackingNumber = trackingNumber;
            return this;
        }
        public Builder setDeliveryStatus(String deliveryStatus) {
            this.deliveryStatus = deliveryStatus;
            return this;
        }
        public Builder setShippingDate(LocalDate shippingDate) {
            this.shippingDate = shippingDate;
            return this;
        }
        public Builder setDeliveryDate(LocalDate deliveryDate) {
            this.deliveryDate = deliveryDate;
            return this;
        }

        public Builder copy(Delivery delivery) {
            this.deliveryId = delivery.getDeliveryId();
            this.shippingMethod = delivery.getShippingMethod();
            this.trackingNumber = delivery.getShippingTrackingNumber();
            this.shippingDate = delivery.getShippingDate();
            this.deliveryDate = delivery.getDeliveryDate();
            this.deliveryStatus = delivery.getShippingDeliveryStatus();
            return this;


        }
        public Delivery build() {
            return new Delivery(this);
        }

    }


}
