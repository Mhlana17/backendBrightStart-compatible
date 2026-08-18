package za.ac.cput.factory;

import za.ac.cput.domain.Admin;
import za.ac.cput.util.Helper;

public class AdminFactory {
    public static Admin createAdmin(String username,String password, String email) {
        if (Helper.isNullOrEmpty(username)
                ) {
            return null;
        }

        if (!Helper.isValidAdminEmail(email)) {
            System.out.println("Invalid admin email format");
            return null;
        }

        if (!Helper.isValidAdminPassword(password)) {
            System.out.println("Invalid admin password");
            return null;
        }

        return new Admin.Builder()
                .setUsername(username)
                .setPassword(password)
                .setEmail(email)
                .build();
    }
}