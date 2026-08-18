package za.ac.cput.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import za.ac.cput.domain.Booking;
import za.ac.cput.repository.BookingRepository;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {
    private final BookingRepository repository;

    public BookingController(BookingRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Booking create(@RequestBody Booking booking) {
        validate(booking);
        booking.setStatus("REQUESTED");
        booking.setPricePerWeek(150.0);
        booking.setTotalPrice(booking.getLearners() * 150.0);
        return repository.save(booking);
    }

    @GetMapping
    public List<Booking> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Booking getById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
    }

    @PutMapping("/{id}")
    public Booking update(@PathVariable Long id, @RequestBody Booking incoming) {
        Booking booking = getById(id);
        booking.setGrade(incoming.getGrade());
        booking.setSessionType(incoming.getSessionType());
        booking.setBookingDate(incoming.getBookingDate());
        booking.setBookingTime(incoming.getBookingTime());
        booking.setLearners(incoming.getLearners());
        booking.setTotalPrice(incoming.getLearners() * 150.0);
        if (incoming.getStatus() != null) booking.setStatus(incoming.getStatus());
        return repository.save(booking);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    private void validate(Booking booking) {
        if (booking.getGrade() == null || !booking.getGrade().matches("Grade [1-3]"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Grade must be Grade 1, Grade 2 or Grade 3");
        if (booking.getSessionType() == null || booking.getSessionType().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Session type is required");
        if (booking.getBookingDate() == null || booking.getBookingTime() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking date and time are required");
        if (booking.getLearners() == null || booking.getLearners() < 1 || booking.getLearners() > 10)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Learners must be between 1 and 10");
    }
}
