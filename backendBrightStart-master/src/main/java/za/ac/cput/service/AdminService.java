package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Admin;
import za.ac.cput.repository.AdminRepository;
import java.util.List;

@Service
public class AdminService implements IAdminService {

    private final AdminRepository repository;

    @Autowired
    public AdminService(AdminRepository repository) {
        this.repository = repository;
    }

    @Override
    public Admin create(Admin admin) {
        return this.repository.save(admin);
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
