package za.ac.cput.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import za.ac.cput.domain.Admin;
import za.ac.cput.domain.Booking;
import za.ac.cput.domain.Progress;
import za.ac.cput.domain.User;
import za.ac.cput.factory.AdminFactory;
import za.ac.cput.repository.BookingRepository;
import za.ac.cput.repository.ProgressRepository;
import za.ac.cput.repository.UserRepository;
import za.ac.cput.security.JwtUtils;
import za.ac.cput.service.AdminService;
import za.ac.cput.util.Helper;

import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final JwtUtils jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ProgressRepository progressRepository;

    public AdminController(
            AdminService adminService,
            JwtUtils jwtTokenUtil,
            PasswordEncoder passwordEncoder,
            BookingRepository bookingRepository,
            UserRepository userRepository,
            ProgressRepository progressRepository
    ) {
        this.adminService = adminService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.progressRepository = progressRepository;
    }

    /*
     * ADMIN LOGIN
     */
    @PostMapping("/signin")
    public Map<String, Object> signIn(
            @RequestBody Map<String, String> credentials
    ) {

        String email = credentials.get("email");
        String password = credentials.get("password");

        if (!Helper.isValidAdminEmail(email) ||
                !Helper.isValidAdminPassword(password)) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid admin credentials"
            );
        }

        Admin admin = adminService.findByEmail(email);

        if (admin == null) {

            String username =
                    email.substring(0, email.indexOf('@'));

            admin = adminService.create(
                    AdminFactory.createAdmin(
                            username,
                            password,
                            email
                    )
            );

        } else if (!passwordEncoder.matches(
                password,
                admin.getPassword()
        )) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid admin credentials"
            );
        }

        return Map.of(
                "token",
                jwtTokenUtil.generateToken(
                        admin.getEmail(),
                        "ADMIN"
                ),

                "adminId",
                admin.getAdminId(),

                "email",
                admin.getEmail(),

                "username",
                admin.getUsername(),

                "role",
                "ADMIN"
        );
    }

    /*
     * BOOKING REQUESTS
     */

    @GetMapping("/bookings")
    public List<Map<String, Object>> getBookingRequests() {

        return bookingRepository.findAll()
                .stream()
                .filter(booking ->
                        "REQUESTED".equalsIgnoreCase(
                                booking.getStatus()
                        )
                )
                .sorted(
                        Comparator.comparing(
                                Booking::getCreatedAt
                        ).reversed()
                )
                .map(this::bookingResponse)
                .toList();
    }

    @PutMapping("/bookings/{id}/approve")
    public Map<String, Object> approveBooking(
            @PathVariable Long id
    ) {

        return changeBookingStatus(
                id,
                "APPROVED"
        );
    }

    @PutMapping("/bookings/{id}/reject")
    public Map<String, Object> rejectBooking(
            @PathVariable Long id
    ) {

        return changeBookingStatus(
                id,
                "REJECTED"
        );
    }

    private Map<String, Object> changeBookingStatus(
            Long id,
            String status
    ) {

        Booking booking =
                bookingRepository.findById(id)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Booking not found"
                                )
                        );

        Booking updated =
                new Booking.Builder()
                        .copy(booking)
                        .setStatus(status)
                        .build();

        return bookingResponse(
                bookingRepository.save(updated)
        );
    }

    private Map<String, Object> bookingResponse(
            Booking booking
    ) {

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "bookingId",
                booking.getBookingId()
        );

        response.put(
                "grade",
                booking.getGrade()
        );

        response.put(
                "sessionType",
                booking.getSessionType()
        );

        response.put(
                "bookingDate",
                booking.getBookingDate()
        );

        response.put(
                "bookingTime",
                booking.getBookingTime()
        );

        response.put(
                "learners",
                booking.getLearners()
        );

        response.put(
                "totalPrice",
                booking.getTotalPrice()
        );

        response.put(
                "status",
                booking.getStatus()
        );

        response.put(
                "createdAt",
                booking.getCreatedAt()
        );

        if (booking.getUser() != null) {

            response.put(
                    "userId",
                    booking.getUser().getUserId()
            );

            response.put(
                    "userName",
                    booking.getUser().getFirstName()
                            + " "
                            + booking.getUser().getLastName()
            );

            response.put(
                    "userEmail",
                    booking.getUser().getEmail()
            );
        }

        return response;
    }

    /*
     * LEARNERS
     */

    @GetMapping("/learners")
    public List<Map<String, Object>> getLearners() {

        return userRepository.findAll()
                .stream()
                .map(user -> {

                    Map<String, Object> learner =
                            new LinkedHashMap<>();

                    learner.put(
                            "userId",
                            user.getUserId()
                    );

                    learner.put(
                            "name",
                            user.getFirstName()
                                    + " "
                                    + user.getLastName()
                    );

                    learner.put(
                            "email",
                            user.getEmail()
                    );

                    learner.put(
                            "phoneNumber",
                            user.getPhoneNumber()
                    );

                    return learner;
                })
                .toList();
    }

    /*
     * GET ONE LEARNER'S PROGRESS
     */

    @GetMapping("/learners/{learnerId}/progress")
    public Map<String, Object> getLearnerProgress(
            @PathVariable Long learnerId
    ) {

        User learner =
                userRepository.findById(learnerId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Learner not found"
                                )
                        );

        Progress progress =
                progressRepository
                        .findByLearner_UserId(learnerId)
                        .orElseGet(
                                () -> new Progress(learner)
                        );

        return progressResponse(
                learner,
                progress
        );
    }

    /*
     * UPDATE LEARNER PROGRESS
     */

    @PutMapping("/learners/{learnerId}/progress")
    public Map<String, Object> updateLearnerProgress(
            @PathVariable Long learnerId,
            @RequestBody Map<String, Object> body
    ) {

        User learner =
                userRepository.findById(learnerId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Learner not found"
                                )
                        );

        Progress progress =
                progressRepository
                        .findByLearner_UserId(learnerId)
                        .orElseGet(
                                () -> new Progress(learner)
                        );

        progress.update(
                integerValue(body, "reading"),
                integerValue(body, "writing"),
                integerValue(body, "comprehension"),
                integerValue(body, "speaking"),
                integerValue(body, "overallProgress"),
                stringValue(body, "status"),
                stringValue(body, "comment"),
                stringValue(body, "tutor")
        );

        Progress saved =
                progressRepository.save(progress);

        return progressResponse(
                learner,
                saved
        );
    }

    private Integer integerValue(
            Map<String, Object> body,
            String key
    ) {

        Object value = body.get(key);

        if (value instanceof Number number) {
            return number.intValue();
        }

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                key + " is required"
        );
    }

    private String stringValue(
            Map<String, Object> body,
            String key
    ) {

        Object value = body.get(key);

        return value == null
                ? ""
                : value.toString();
    }

    private Map<String, Object> progressResponse(
            User learner,
            Progress progress
    ) {

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "learnerId",
                learner.getUserId()
        );

        response.put(
                "learnerName",
                learner.getFirstName()
                        + " "
                        + learner.getLastName()
        );

        response.put(
                "email",
                learner.getEmail()
        );

        response.put(
                "overallProgress",
                progress.getOverallProgress()
        );

        response.put(
                "status",
                progress.getStatus() == null
                        ? "Not started"
                        : progress.getStatus()
        );

        response.put(
                "reading",
                progress.getReading()
        );

        response.put(
                "writing",
                progress.getWriting()
        );

        response.put(
                "comprehension",
                progress.getComprehension()
        );

        response.put(
                "speaking",
                progress.getSpeaking()
        );

        response.put(
                "comment",
                progress.getComment() == null
                        ? ""
                        : progress.getComment()
        );

        response.put(
                "tutor",
                progress.getTutor() == null
                        ? ""
                        : progress.getTutor()
        );

        response.put(
                "commentDate",
                progress.getCommentDate() == null
                        ? ""
                        : progress.getCommentDate().toString()
        );

        return response;
    }
}