package com.prosa.rivertech.rest.bankservices.utils;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.prosa.rivertech.rest.bankservices.entity.Account;
import com.prosa.rivertech.rest.bankservices.exception.UnauthorizedException;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class AuthorizationManager {

    public boolean validateUser(String authorization, Account account) {
        byte[] userPassBytes = Base64.getDecoder().decode(authorization.substring(6).trim());
        String[] userPass = new String(userPassBytes).split(":");
        String username = userPass.length > 0 ? userPass[0] : "";
        String password = userPass.length > 1 ? userPass[1] : "";
        return account.getNumber().equalsIgnoreCase(username) && BCrypt.checkpw(password, account.getPassword());
    }
}
