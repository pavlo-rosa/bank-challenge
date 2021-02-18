package com.prosa.rivertech.rest.bankservices.utils;

import com.prosa.rivertech.rest.bankservices.entity.Account;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class AuthorizationManager {

    public boolean validateUser(String authorization, Account account) {
        if (authorization == null || authorization.length() < 6 || account.getNumber() == null || account.getPassword() == null) {
            return false;
        }
        byte[] userPassBytes = Base64.getDecoder().decode(authorization.substring(6).trim());
        String[] userPass = new String(userPassBytes).split(":");
        String username = userPass.length > 0 ? userPass[0] : "";
        String password = userPass.length > 1 ? userPass[1] : "";
        return account.getNumber().equalsIgnoreCase(username) && BCrypt.checkpw(password, account.getPassword());
    }
}
