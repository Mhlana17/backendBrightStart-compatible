package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Admin;
import za.ac.cput.repository.AdminRepository;
import java.util.List;

@Service
public class AdminService implements IAdminService {

    private final AdminRepository repository;
    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    @Autowired
    public AdminService(AdminRepository repository) {
        this.repository = repository;
    }

    @Override
    public Admin create(Admin admin) {
        return this.repository.save(withHashedPassword(admin));
    }

    @Override
    public Admin read(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    public Admin update(Admin admin) {
        return this.repository.save(withHashedPassword(admin));
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

    private Admin withHashedPassword(Admin admin) {
        if (admin == null || isBcryptHash(admin.getPassword())) {
            return admin;
        }

        return new Admin.Builder()
                .copy(admin)
                .setPassword(passwordEncoder.encode(admin.getPassword()))
                .build();
    }

    private boolean isBcryptHash(String password) {
        return password != null && password.matches("^\\$2[aby]\\$.{56}$");
    }
}
