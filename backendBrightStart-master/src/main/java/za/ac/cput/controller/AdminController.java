package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import za.ac.cput.domain.Admin;
import za.ac.cput.factory.AdminFactory;
import za.ac.cput.security.JwtUtils;
import za.ac.cput.service.AdminService;
import za.ac.cput.util.Helper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {
    private final AdminService adminService;
    private final JwtUtils jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(AdminService adminService, JwtUtils jwtTokenUtil, PasswordEncoder passwordEncoder) {
        this.adminService = adminService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/create")
    public Admin create(@RequestBody Admin admin) {
        // Your AdminService should now handle the password hashing
        return adminService.create(admin);
    }

    @GetMapping("/read/{userId}")
    public Admin read(@PathVariable Long userId) {
        return adminService.read(userId);
    }

    @PostMapping("/update")
    public Admin update(@RequestBody Admin admin) {
        return adminService.update(admin);
    }

    @DeleteMapping("/delete/{userId}")
    public void delete(@PathVariable Long userId) {
        adminService.delete(userId);
    }

    @GetMapping("/getAll")
    public List<Admin> getAll() {
        return adminService.getAll();
    }

    @PostMapping("/signin")
    public Map<String, Object> signIn(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        if (!Helper.isValidAdminEmail(email) || !Helper.isValidAdminPassword(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid admin credentials format");
        }

        Admin admin = adminService.findByEmail(email);

        if (admin == null) {
            // If admin does not exist, create a new one.
            // The AdminService's 'create' method is responsible for hashing the password.
            String username = email.split("@")[0];
            String name = username;
            // The password passed here is the plain-text one, which is correct.
            admin = AdminFactory.createAdmin(username, password, email);

            if (admin == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create admin from factory");
            }
            admin = adminService.create(admin);

        } else {
            // If admin already exists, verify the password using the encoder.
            if (!passwordEncoder.matches(password, admin.getPassword())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid admin credentials");
            }
        }

        // If we get here, the credentials are valid.
        String token = jwtTokenUtil.generateToken(admin.getEmail(), "ADMIN");

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", admin.getUserId());
        response.put("email", admin.getEmail());
        response.put("username", admin.getUsername());
        response.put("role", "ADMIN");

        return response;
    }
}