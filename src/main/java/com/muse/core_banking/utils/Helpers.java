package com.muse.core_banking.utils;

import com.muse.core_banking.entities.Transaction;
import com.muse.core_banking.entities.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.core.Authentication;

import java.beans.PropertyDescriptor;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Helpers {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateOtp(int length){
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i<length; i++){
            int index = RANDOM.nextInt(alphabet.length());
            stringBuilder.append(alphabet.charAt(index));
        }
        return stringBuilder.toString();
    }

    public static String generateTransactionRef(){

        return "TX" + System.currentTimeMillis();
    }

    public static User getConnectedUser(Authentication connectUser){
        return (User) connectUser.getPrincipal();
    }

    public static <T> T copyNonNullProperties(Object source, T target) {
         BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
         return target;
    }

    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }

        return emptyNames.toArray(new String[0]);
    }

    public static String accountGenerator(){
        var defaultNo = 0011564;
        int randomPart = RANDOM.nextInt(1000); // Generates 0 - 999


        return String.format("%s%03d", defaultNo, randomPart);
    }

    public static Long sumDailyTransaction(List<Transaction> transactions){
        var amount = 0L;
        for (var t: transactions){
            amount += t.getAmount();
        }
        return amount;
    }
}
