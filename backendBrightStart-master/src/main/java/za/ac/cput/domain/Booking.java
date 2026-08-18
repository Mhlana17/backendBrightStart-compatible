package za.ac.cput.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @Column(nullable = false)
    private String grade;

    @Column(nullable = false)
    private String sessionType;

    @Column(nullable = false)
    private LocalDate bookingDate;

    @Column(nullable = false)
    private LocalTime bookingTime;

    @Column(nullable = false)
    private Integer learners;

    @Column(nullable = false)
    private Double pricePerWeek;

    @Column(nullable = false)
    private Double totalPrice;

    @Column(nullable = false)
    private String status = "REQUESTED";

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    protected Booking() {}

    public Long getBookingId() {
        return bookingId;
    }
    public String getGrade() {
        return grade;
    }
    public String getSessionType() { return sessionType; }
    public LocalDate getBookingDate() { return bookingDate; }
    public LocalTime getBookingTime() { return bookingTime; }
    public Integer getLearners() { return learners; }
    public Double getPricePerWeek() { return pricePerWeek; }
    public Double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setGrade(String grade) { this.grade = grade; }
    public void setSessionType(String sessionType) { this.sessionType = sessionType; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }
    public void setBookingTime(LocalTime bookingTime) { this.bookingTime = bookingTime; }
    public void setLearners(Integer learners) { this.learners = learners; }
    public void setPricePerWeek(Double pricePerWeek) { this.pricePerWeek = pricePerWeek; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
    public void setStatus(String status) { this.status = status; }
}
