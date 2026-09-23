package za.ac.cput.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Booking;
import za.ac.cput.domain.User;
import za.ac.cput.repository.UserRepository;
import za.ac.cput.service.BookingService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final UserRepository userRepository;

    public BookingController(
            BookingService bookingService,
            UserRepository userRepository
    ) {
        this.bookingService = bookingService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> createBooking(
            @RequestBody Booking booking
    ) {

        User user = userRepository.findAll().stream().findFirst().orElse(null);

        if (user == null) {
            return ResponseEntity.status(401).body(
                    Map.of("message", "User account could not be found.")
            );
        }

        Booking bookingToSave = new Booking.Builder()
                .copy(booking)
                .setBookingId(null)
                .setUser(user)
                .setStatus("REQUESTED")
                .build();

        Booking saved = bookingService.create(bookingToSave);

        return ResponseEntity.ok(toResponse(saved));
    }

    @GetMapping("/mine")
    public ResponseEntity<?> getMyBookings() {
        return ResponseEntity.ok(
                bookingService.getAll()
                        .stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    private Map<String, Object> toResponse(Booking booking) {

        Map<String, Object> response = new HashMap<>();

        response.put("bookingId", booking.getBookingId());
        response.put("grade", booking.getGrade());
        response.put("sessionType", booking.getSessionType());
        response.put("bookingDate", booking.getBookingDate());
        response.put("bookingTime", booking.getBookingTime());
        response.put("learners", booking.getLearners());
        response.put("pricePerWeek", booking.getPricePerWeek());
        response.put("totalPrice", booking.getTotalPrice());
        response.put("status", booking.getStatus());
        response.put("createdAt", booking.getCreatedAt());

        if (booking.getUser() != null) {
            response.put("userId", booking.getUser().getUserId());
            response.put(
                    "userName",
                    booking.getUser().getFirstName() + " " +
                            booking.getUser().getLastName()
            );
            response.put("userEmail", booking.getUser().getEmail());
        }

        return response;
    }
}
