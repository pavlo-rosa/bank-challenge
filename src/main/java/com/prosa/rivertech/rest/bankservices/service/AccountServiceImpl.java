package com.prosa.rivertech.rest.bankservices.service;

import com.prosa.rivertech.rest.bankservices.entity.Account;
import com.prosa.rivertech.rest.bankservices.entity.User;
import com.prosa.rivertech.rest.bankservices.exception.NotFoundException;
import com.prosa.rivertech.rest.bankservices.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.*;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account findById(Long id) {
        Optional<Account> result = accountRepository.findById(id);
        Account account = null;
        if (result.isPresent()) {
            account = result.get();
        } else {
            throw new NotFoundException("Account id not found - " + id);
        }
        return account;
    }

    @Override
    public Account createAccount(User user, String password) {
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        String newNumberAccount = generateNumberAccount();
        Account newAccount = new Account(user, encodedPassword, newNumberAccount);
        return accountRepository.save(newAccount);
    }

    //3-random-digits + timestamp
    //Generate number without collision.
    private String generateNumberAccount() {
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


//    Disabled
//    @Override
//    public Account update(Account account) {
//        if (account.getId() == null || account.getId() <= 0) {
//            throw new BadRequestException("Missing user id");
//        }
//        String encodedPassword = new BCryptPasswordEncoder().encode(account.getPassword());
//        account.setPassword(encodedPassword);
//        return accountRepository.save(account);
//    }

    @Override
    public void delete(Long id) {
        Account account = this.findById(id);
        if (account != null) {
            accountRepository.deleteById(id);
        }

    }
}
