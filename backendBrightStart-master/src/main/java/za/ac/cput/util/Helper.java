package za.ac.cput.util;

import java.util.regex.Pattern;

public final class Helper {

    private Helper() {
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return !isNullOrEmpty(email)
                && Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", email);
    }

    public static boolean isValidAdminEmail(String email) {
        return isValidEmail(email) && email.equals(email.toLowerCase());
    }

    public static boolean isValidAdminPassword(String password) {
        return "naidoo_1@dev".equals(password);
    }
}
