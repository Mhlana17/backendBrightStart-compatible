package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.User;
import za.ac.cput.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // SignUp
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> signupRequest) {
        String firstName = signupRequest.get("firstName");
        String lastName = signupRequest.get("lastName");
        String email = signupRequest.get("email").toLowerCase();
        String password = signupRequest.get("password");
        String address = signupRequest.get("address");
        String phoneNumber = signupRequest.get("phoneNumber");

        User user = new User.Builder()
                .setUsername(email.substring(0, email.indexOf('@')))
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setPassword(password)
                .setAddress(address)
                .setPhoneNumber(phoneNumber)
                .build();

        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    // SignIn
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email").toLowerCase();
        String password = loginRequest.get("password");

        return userRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(password))
                .<ResponseEntity<?>>map(u -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("token", u.getUserId());
                    response.put("name", u.getFirstName() + " " + u.getLastName());
                    response.put("email", u.getEmail());
                    response.put("role", "USER");
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(401).body(Map.of("message", "Invalid email or password")));
    }
}
