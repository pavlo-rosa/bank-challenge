package com.prosa.rivertech.rest.bankservices.utils;

import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Date;

@Component
public class GeneratorAccountsNumberManager {

    //3-random-digits + timestamp
    //Generate number without collision.
    public String generateNumber() {
        long timestamp = new Date().getTime();
        //Extra random numbers
        int min = 100;
        int max = 999;
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            byte[] randomBytes = new byte[64];
            random.nextBytes(randomBytes);
            int randInRange = random.nextInt((max - min) + 1) + min;
            return randInRange +Long.toString(timestamp);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
            throw new RuntimeException("Internal Error");
        }
    }
}
