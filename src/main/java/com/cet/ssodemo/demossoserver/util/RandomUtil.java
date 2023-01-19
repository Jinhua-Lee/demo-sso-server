package com.cet.ssodemo.demossoserver.util;

import java.security.SecureRandom;
import java.util.Random;

public class RandomUtil {

    private static final Random SECURE_RANDOM = new SecureRandom();

    private static final String ORIGIN = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String randomString(int size) {
        if (size < 0) {
            return null;
        }
        if (size == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            builder.append(ORIGIN.charAt(SECURE_RANDOM.nextInt(ORIGIN.length())));
        }
        return builder.toString();
    }
}
