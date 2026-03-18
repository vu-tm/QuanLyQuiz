package helper;

import java.util.regex.Pattern;

public class ValidationHelper {
    
    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }
    
    public static boolean isId(String id) {
        return id != null && id.matches("^[0-9]+$");
    }
    
    // THÊM PHƯƠNG THỨC NÀY
    public static boolean isUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        // Username chỉ chứa chữ, số, dấu gạch dưới, độ dài 3-20 ký tự
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }
    
    public static boolean isEmail(String email) {
        if (email == null) return false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }
    
    public static boolean isPhoneNumber(String phone) {
        if (phone == null) return false;
        String phoneRegex = "^(0|84)[0-9]{9}$";
        return Pattern.matches(phoneRegex, phone);
    }
    
    public static boolean isDate(String date) {
        if (date == null) return false;
        return date.matches("^\\d{4}-\\d{2}-\\d{2}$");
    }
}