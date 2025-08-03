package com.muse.core_banking.utils;

import java.security.SecureRandom;
import java.util.Random;

public class Helpers {

    public static String generateOtp(int length){
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i<length; i++){
            int index = random.nextInt(alphabet.length());
            stringBuilder.append(alphabet.charAt(index));
        }
        return stringBuilder.toString();
    }
}
