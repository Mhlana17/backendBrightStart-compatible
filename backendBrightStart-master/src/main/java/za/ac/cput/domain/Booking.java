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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

    private Booking(Builder builder) {
        this.bookingId = builder.bookingId;
        this.user = builder.user;
        this.grade = builder.grade;
        this.sessionType = builder.sessionType;
        this.bookingDate = builder.bookingDate;
        this.bookingTime = builder.bookingTime;
        this.learners = builder.learners;
        this.pricePerWeek = builder.pricePerWeek;
        this.totalPrice = builder.totalPrice;
        this.status = builder.status;
        this.createdAt = builder.createdAt;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public User getUser() {
        return user;
    }

    public String getGrade() {
        return grade;
    }

    public String getSessionType() {
        return sessionType;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public LocalTime getBookingTime() {
        return bookingTime;
    }

    public Integer getLearners() {
        return learners;
    }

    public Double getPricePerWeek() {
        return pricePerWeek;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public static class Builder {

        private Long bookingId;
        private User user;
        private String grade;
        private String sessionType;
        private LocalDate bookingDate;
        private LocalTime bookingTime;
        private Integer learners;
        private Double pricePerWeek = 150.0;
        private Double totalPrice;
        private String status = "REQUESTED";
        private LocalDateTime createdAt = LocalDateTime.now();

        public Builder setBookingId(Long bookingId) {
            this.bookingId = bookingId;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public Builder setGrade(String grade) {
            this.grade = grade;
            return this;
        }

        public Builder setSessionType(String sessionType) {
            this.sessionType = sessionType;
            return this;
        }

        public Builder setBookingDate(LocalDate bookingDate) {
            this.bookingDate = bookingDate;
            return this;
        }

        public Builder setBookingTime(LocalTime bookingTime) {
            this.bookingTime = bookingTime;
            return this;
        }

        public Builder setLearners(Integer learners) {
            this.learners = learners;
            return this;
        }

        public Builder setPricePerWeek(Double pricePerWeek) {
            this.pricePerWeek = pricePerWeek;
            return this;
        }

        public Builder setTotalPrice(Double totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder copy(Booking booking) {
            this.bookingId = booking.bookingId;
            this.user = booking.user;
            this.grade = booking.grade;
            this.sessionType = booking.sessionType;
            this.bookingDate = booking.bookingDate;
            this.bookingTime = booking.bookingTime;
            this.learners = booking.learners;
            this.pricePerWeek = booking.pricePerWeek;
            this.totalPrice = booking.totalPrice;
            this.status = booking.status;
            this.createdAt = booking.createdAt;

            return this;
        }

        public Booking build() {
            return new Booking(this);
        }
    }
}