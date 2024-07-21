package voyagers.utils;

import java.security.SecureRandom;
import java.util.Random;

public class Utils { public static String getRandomEmail() {
    char[] chars = "0123456789abcdef".toCharArray();
    Random random = new SecureRandom();
    char[] result = new char[8];
    for (int i = 0; i < result.length; i++) {
        int randomIndex = random.nextInt(chars.length);
        result[i] = chars[randomIndex];
    }
    String email = new String(result) + "@test.com";
    return email;
}

    public static boolean isNullOrEmpty(String value) {
        return value == null || "".equals(value);
    }
}

