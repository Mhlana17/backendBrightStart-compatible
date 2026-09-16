package za.ac.cput.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import za.ac.cput.domain.Booking;
import za.ac.cput.service.IBookingService;

import java.util.List;

@RestController
@RequestMapping("api/bookings")
public class BookingController {
    private final IBookingService service;

    public BookingController(IBookingService service) {
        this.service = service;
    }

    @PostMapping
    public Booking create(@RequestBody Booking booking) {
        try {
            return service.create(booking);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }

    @GetMapping
    public List<Booking> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Booking getById(@PathVariable Long id) {
        Booking booking = service.read(id);
        if (booking == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found");
        }
        return booking;
    }

    @PutMapping("/{id}")
    public Booking update(@PathVariable Long id, @RequestBody Booking incoming) {
        Booking existing = getById(id);
        try {
            Booking updated = new Booking.Builder()
                    .copy(incoming)
                    .setBookingId(existing.getBookingId())
                    .build();
            Booking result = service.update(updated);
            if (result == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found");
            }
            return result;
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!service.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found");
        }
    }
}
