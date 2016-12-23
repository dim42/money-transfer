package pack.transfer.util;

public class Utils {
    public static void requireNonBlank(String obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
