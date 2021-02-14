package com.prosa.rivertech.rest.bankservices.service;

import com.prosa.rivertech.rest.bankservices.entity.Account;
import com.prosa.rivertech.rest.bankservices.entity.Account;
import com.prosa.rivertech.rest.bankservices.repository.AccountRepository;
import com.prosa.rivertech.rest.bankservices.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sun.security.util.Password;

import java.security.SecureRandom;
import java.sql.Timestamp;
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

//        Random generator = new Random(System.currentTimeMillis());
//        System.out.println(generator.nextLong() % 1000000000000L);
        try{
            UUID uuid = UUID.randomUUID();
            String uuidAsString = uuid.toString();

            System.out.println("Your UUID is: " + uuidAsString);
        }catch(Exception e){e.printStackTrace();}

        return accountRepository.findAll();
    }

    @Override
    public Account findById(Long id) {
        Optional<Account> result = accountRepository.findById(id);
        Account account = null;
        if (result.isPresent()) {
            account = result.get();
        } else {
            throw new RuntimeException("Did not find account id: " + id);
        }
        return account;
    }

    @Override
    public Account save(Account account) {
        String encodedPassword= new BCryptPasswordEncoder().encode(account.getPassword());
        account.setPassword(encodedPassword);
        return accountRepository.save(account);
    }

    @Override
    public void delete(Long id) {
        accountRepository.deleteById(id);
    }
}
