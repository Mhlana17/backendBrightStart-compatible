package za.ac.cput.service;

import za.ac.cput.domain.User;

import java.util.Optional;

public interface IUserService extends  IService<User, Long> {
    Optional<User> findByEmail(String email);

    User register(String firstName, String lastName, String email,
                  String password, String address, String phoneNumber);

    Optional<User> authenticate(String email, String password);
}
