package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Admin;
import za.ac.cput.repository.AdminRepository;

import java.util.List;

@Service
public class AdminService implements IAdminService {

    private final AdminRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(AdminRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Admin create(Admin admin) {
        // 1. Hash the plain-text password
        String hashedPassword = passwordEncoder.encode(admin.getPassword());

        // 2. Use the existing copy() method and setPassword() to create a new Admin
        //    with the hashed password
        Admin adminWithHashedPassword = new Admin.Builder()
                .copy(admin)  // Copy all existing fields
                .setPassword(hashedPassword)  // Override with hashed password
                .build();

        // 3. Save and return the admin with the hashed password
        return this.repository.save(adminWithHashedPassword);
    }

    @Override
    public Admin read(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    public Admin update(Admin admin) {
        // If you allow password updates, you should hash the password here too.
        return this.repository.save(admin);
    }

    @Override
    public boolean delete(Long id) {
        this.repository.deleteById(id);
        return true;
    }

    @Override
    public List<Admin> getAll() {
        return this.repository.findAll();
    }

    public Admin findByEmail(String email) {
        return repository.findByEmail(email);
    }
}