package za.ac.cput.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.User;
import za.ac.cput.repository.UserRepository;
import za.ac.cput.security.JwtUtils;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            UserRepository userRepository,
            JwtUtils jwtUtils,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody Map<String, String> request
    ) {

        String firstName = request.getOrDefault("firstName", "").trim();
        String lastName = request.getOrDefault("lastName", "").trim();
        String email = request.getOrDefault("email", "").trim().toLowerCase();
        String password = request.getOrDefault("password", "").trim();
        String address = request.getOrDefault("address", "").trim();
        String phoneNumber = request.getOrDefault("phoneNumber", "").trim();

        if (firstName.isBlank()
                || lastName.isBlank()
                || email.isBlank()
                || password.isBlank()) {

            return ResponseEntity.badRequest().body(
                    Map.of(
                            "message",
                            "First name, last name, email and password are required."
                    )
            );
        }

        if (!email.matches(
                "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"
        )) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "message",
                            "Please enter a valid email address."
                    )
            );
        }

        if (password.length() < 8) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "message",
                            "Password must be at least 8 characters."
                    )
            );
        }

        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.status(409).body(
                    Map.of(
                            "message",
                            "An account with this email already exists."
                    )
            );
        }

        String username =
                email.substring(0, email.indexOf('@'));

        User user = new User.Builder()
                .setUsername(username)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setPassword(
                        passwordEncoder.encode(password)
                )
                .setAddress(address)
                .setPhoneNumber(phoneNumber)
                .build();

        User savedUser = userRepository.save(user);

        return ResponseEntity.status(201).body(
                Map.of(
                        "message", "Registration successful.",
                        "user", publicUser(savedUser)
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> request
    ) {

        String email =
                request.getOrDefault("email", "")
                        .trim()
                        .toLowerCase();

        String password =
                request.getOrDefault("password", "").trim();

        return userRepository.findByEmail(email)
                .filter(user ->
                        passwordEncoder.matches(
                                password,
                                user.getPassword()
                        )
                )
                .<ResponseEntity<?>>map(user -> {

                    Map<String, Object> response =
                            new HashMap<>();

                    response.put(
                            "token",
                            jwtUtils.generateToken(
                                    user.getEmail(),
                                    "USER"
                            )
                    );

                    response.put(
                            "user",
                            publicUser(user)
                    );

                    return ResponseEntity.ok(response);
                })
                .orElse(
                        ResponseEntity
                                .status(401)
                                .body(
                                        Map.of(
                                                "message",
                                                "Invalid email or password."
                                        )
                                )
                );
    }

    private Map<String, Object> publicUser(User user) {

        Map<String, Object> data =
                new HashMap<>();

        data.put("userId", user.getUserId());
        data.put("firstName", user.getFirstName());
        data.put("lastName", user.getLastName());

        data.put(
                "name",
                user.getFirstName()
                        + " "
                        + user.getLastName()
        );

        data.put("email", user.getEmail());
        data.put("address", user.getAddress());
        data.put("phoneNumber", user.getPhoneNumber());
        data.put("role", "USER");

        return data;
    }
}