package za.ac.cput.factory;

import za.ac.cput.domain.User;
import za.ac.cput.util.Helper;

public class UserFactory {
    public static User createCustomer(String username, String name, String password, String email, String address, String phoneNumber) {
        if (Helper.isNullOrEmpty(username) ||
                Helper.isNullOrEmpty(password) ||
                Helper.isNullOrEmpty(name) ||
                Helper.isValidEmail(email) ||
                Helper.isNullOrEmpty(address) ||
                Helper.isNullOrEmpty(phoneNumber)) {
            return null;
        }

        return new User.Builder()
                .setUsername(username)
                .setFirstName(name)
                .setPassword(password)
                .setEmail(email)
                .setAddress(address)
                .setPhoneNumber(phoneNumber)
                .build();
    }
}
