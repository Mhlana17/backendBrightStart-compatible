package za.ac.cput.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.ac.cput.domain.User;
import za.ac.cput.security.JwtUtils;
import za.ac.cput.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    public UserController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        try {
            User savedUser = userService.register(
                    request.get("firstName"), request.get("lastName"),
                    request.get("email"), request.get("password"),
                    request.get("address"), request.get("phoneNumber")
            );
            return ResponseEntity.status(201).body(Map.of(
                    "message", "Registration successful.",
                    "user", publicUser(savedUser)
            ));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
        } catch (IllegalStateException exception) {
            return ResponseEntity.status(409).body(Map.of("message", exception.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        return userService.authenticate(request.get("email"), request.get("password"))
                .<ResponseEntity<?>>map(user -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("token", jwtUtils.generateToken(user.getEmail(), "USER"));
                    response.put("user", publicUser(user));
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> ResponseEntity.status(401).body(Map.of(
                        "message", "Invalid email or password."
                )));
    }

    private Map<String, Object> publicUser(User user) {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", user.getUserId());
        data.put("firstName", user.getFirstName());
        data.put("lastName", user.getLastName());
        data.put("name", user.getFirstName() + " " + user.getLastName());
        data.put("email", user.getEmail());
        data.put("address", user.getAddress());
        data.put("phoneNumber", user.getPhoneNumber());
        data.put("role", "USER");
        return data;
    }
}
