package za.ac.cput.service;

import org.springframework.stereotype.Service;
import za.ac.cput.domain.Booking;
import za.ac.cput.repository.BookingRepository;

import java.util.List;

@Service
public class BookingService implements IBookingService {

    private static final double PRICE_PER_WEEK = 150.0;
    private final BookingRepository repository;

    public BookingService(BookingRepository repository) {
        this.repository = repository;
    }

    @Override
    public Booking create(Booking booking) {

        validate(booking);

        return repository.save(
                new Booking.Builder()
                        .copy(booking)
                        .setBookingId(null)
                        .setStatus("REQUESTED")
                        .setPricePerWeek(PRICE_PER_WEEK)
                        .setTotalPrice(
                                booking.getLearners()
                                        * PRICE_PER_WEEK
                        )
                        .build()
        );
    }

    @Override
    public Booking read(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Booking update(Booking booking) {
        if (booking == null || booking.getBookingId() == null || read(booking.getBookingId()) == null) {
            return null;
        }
        validate(booking);
        Booking existing = read(booking.getBookingId());
        return repository.save(new Booking.Builder()
                .copy(existing)
                .setGrade(booking.getGrade())
                .setSessionType(booking.getSessionType())
                .setBookingDate(booking.getBookingDate())
                .setBookingTime(booking.getBookingTime())
                .setLearners(booking.getLearners())
                .setTotalPrice(booking.getLearners() * PRICE_PER_WEEK)
                .setStatus(booking.getStatus() != null ? booking.getStatus() : existing.getStatus())
                .build());
    }

    @Override
    public boolean delete(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    @Override
    public List<Booking> getAll() {
        return repository.findAll();
    }

    private void validate(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking is required");
        }
        if (booking.getGrade() == null || !booking.getGrade().matches("Grade [1-3]")) {
            throw new IllegalArgumentException("Grade must be Grade 1, Grade 2 or Grade 3");
        }
        if (booking.getSessionType() == null || booking.getSessionType().isBlank()) {
            throw new IllegalArgumentException("Session type is required");
        }
        if (booking.getBookingDate() == null || booking.getBookingTime() == null) {
            throw new IllegalArgumentException("Booking date and time are required");
        }
        if (booking.getLearners() == null || booking.getLearners() < 1 || booking.getLearners() > 10) {
            throw new IllegalArgumentException("Learners must be between 1 and 10");
        }
    }
}
