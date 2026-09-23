package za.ac.cput.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import za.ac.cput.domain.User;
import za.ac.cput.repository.UserRepository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User create(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User is required.");
        }
        return repository.save(withHashedPassword(user));
    }

    @Override
    public User read(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public User update(User user) {
        if (user == null || user.getUserId() == null || !repository.existsById(user.getUserId())) {
            return null;
        }
        return repository.save(withHashedPassword(user));
    }

    @Override
    public boolean delete(Long id) {
        if (id == null || !repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(normalizeEmail(email));
    }

    @Override
    public User register(String firstName, String lastName, String email,
                         String password, String address, String phoneNumber) {
        String normalizedFirstName = required(firstName, "First name");
        String normalizedLastName = required(lastName, "Last name");
        String normalizedEmail = normalizeEmail(email);
        String normalizedPassword = required(password, "Password");

        if (!normalizedEmail.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("Please enter a valid email address.");
        }
        if (normalizedPassword.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters.");
        }
        if (repository.findByEmail(normalizedEmail).isPresent()) {
            throw new IllegalStateException("An account with this email already exists.");
        }

        String username = normalizedEmail.substring(0, normalizedEmail.indexOf('@'));
        User user = new User.Builder()
                .setUsername(username)
                .setFirstName(normalizedFirstName)
                .setLastName(normalizedLastName)
                .setEmail(normalizedEmail)
                .setPassword(passwordEncoder.encode(normalizedPassword))
                .setAddress(trim(address))
                .setPhoneNumber(trim(phoneNumber))
                .build();
        return repository.save(user);
    }

    @Override
    public Optional<User> authenticate(String email, String password) {
        return findByEmail(email);
    }

    private String normalizeEmail(String email) {
        return trim(email).toLowerCase(Locale.ROOT);
    }

    private String required(String value, String fieldName) {
        String normalizedValue = trim(value);
        if (normalizedValue.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
        return normalizedValue;
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private User withHashedPassword(User user) {
        if (user == null || isBcryptHash(user.getPassword())) {
            return user;
        }

        return new User.Builder()
                .copy(user)
                .setPassword(passwordEncoder.encode(user.getPassword()))
                .build();
    }

    private boolean isBcryptHash(String password) {
        return password != null && password.matches("^\\$2[aby]\\$.{56}$");
    }
}
